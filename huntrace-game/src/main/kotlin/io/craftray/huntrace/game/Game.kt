package io.craftray.huntrace.game

import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.collection.HunterTargetCollection
import io.craftray.huntrace.game.collection.InternalMutableSet
import io.craftray.huntrace.game.collection.PlayerDataCollection
import io.craftray.huntrace.game.collection.WorldCollection
import io.craftray.huntrace.game.event.HuntraceGameFinishEvent
import io.craftray.huntrace.game.event.HuntraceGamePlayerQuitEvent
import io.craftray.huntrace.game.event.HuntraceGameStartEvent
import io.craftray.huntrace.game.listener.HuntraceGameMainListener
import io.craftray.huntrace.game.listener.HuntraceGamePrepareStateListener
import io.craftray.huntrace.game.multiverse.MultiverseManager
import io.craftray.huntrace.game.schedular.CompassUpdater
import io.craftray.huntrace.rule.RuleSet
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class Game(rules: RuleSet) {
    val gameID = UUID.randomUUID()!!
    private lateinit var compassUpdater: CompassUpdater
    private lateinit var worldController: GameWorldController
    private lateinit var mainListener: HuntraceGameMainListener
    private lateinit var prepareListener: HuntraceGamePrepareStateListener
    private val players = PlayerDataCollection()

    internal lateinit var hunterTargets: HunterTargetCollection
        private set
        @JvmName("-getHunterTargets") get

    var state = State.WAITING
        private set

    var startTime by Delegates.notNull<Long>()
        private set
    var endTime by Delegates.notNull<Long>()
        private set
    lateinit var worlds: WorldCollection
        private set
    var rules = rules
        private set

    val survivors
        get() = this.players.survivors

    val hunters
        get() = this.players.hunters

    val spectators
        get() = this.players.spectators

    val allPlayers
        get() = this.survivors + this.hunters + this.spectators

    /**
     * Initialize a game
     * Generate three dimension, setup CompassUpdater, GameWorldController and GameResultMatcher
     * @author Kylepoops
     * @return the game for call chaining
     */
    fun init(): Game {
        val start = System.currentTimeMillis()
        // lots of checking
        check(this.state == State.WAITING) { "Game is already initialized" }
        this.rules = this.rules.immutableCopy()
        this.hunterTargets = HunterTargetCollection(this)
        this.worldController = GameWorldController(this)
        // we don't want other class to modify the world collection
        this.worlds = this.worldController.generateWorlds()
        this.worldController.linkWorlds()
        this.compassUpdater = CompassUpdater(this)
        this.mainListener = HuntraceGameMainListener(this).also { it.register() }
        this.state = State.INITIALIZED
        Bukkit.getLogger().info("Initialized game ${this.gameID} in ${System.currentTimeMillis() - start}ms")
        return this
    }

    /**
     * Start an initialized io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not initialized or already started
     * @return the game for call chaining
     */
    @Throws(IllegalStateException::class)
    fun start(): Game {
        val start = System.currentTimeMillis()
        check(this.state == State.INITIALIZED) { "Game is not initialized" }
        // lock the player collection to prevent modification after the game is started to prevent issues
        // except for spectators
        this.players.lock()
        // store the player's location, and we'll teleport them back after the game is finished
        this.players.storeLocation()
        runningGame.add(this)
        this.teleportTo()
        this.turnGameModeTo()
        this.compassUpdater.start()
        this.startTime = System.currentTimeMillis()
        thread(true) { HuntraceGameStartEvent(this).callEvent() }
        this.state = State.PREPARING
        this.prepare()
        Bukkit.getLogger().info("Started game ${this.gameID} in ${System.currentTimeMillis() - start}ms")
        return this
    }

    /**
     * Prepare a game and turn it to running state after 10s
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not preparing
     */
    private fun prepare() {
        check(this.state == State.PREPARING) { "Game is not preparing" }
        // an external listener running only during the preparing state
        // it should cancel any interaction for a certain time
        this.prepareListener = HuntraceGamePrepareStateListener(this).also { it.register() }
        // should be rewritten with a better way
        bukkitRunnableOf {
            this.prepareListener.unregister()
            this.state = State.RUNNING
        }.runTaskLater(plugin, 200L)
    }

    /**
     * Finish a game
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
     */
    @JvmName("-finish")
    internal fun finish(result: GameResult) {
        val start = System.currentTimeMillis()
        check(this.state == State.RUNNING) { "Game is not started" }
        this.mainListener.unregister()
        this.turnGameModeFrom()
        this.teleportFrom()
        this.compassUpdater.stop()
        this.worldController.unlinkWorlds()
        this.worldController.deleteWorlds()
        this.endTime = System.currentTimeMillis()
        runningGame.remove(this)
        thread(true) { HuntraceGameFinishEvent(this, result).callEvent() }
        Bukkit.getLogger().info("Finished game ${this.gameID} in ${System.currentTimeMillis() - start}ms")
        this.state = State.FINISHED
    }

    /**
     * Abort the io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the game is not started
     */
    @Throws(IllegalStateException::class)
    fun abort() = finish(GameResult.ABORT)

    /**
     * Make a player quit the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the player to quit
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
     */
    @Throws(IllegalStateException::class)
    fun quit(player: Player): Boolean {
        check(this.state == State.RUNNING) { "Game is not started" }

        // prevent any team from being empty
        return if (player in this.survivors && this.survivors.size <= 1) {
            this.finish(GameResult.SURVIVOR_QUIT)
            true
        } else if (player in this.hunters && this.hunters.size <= 1) {
            this.finish(GameResult.HUNTER_QUIT)
            true
        } else if (player in this.hunters && this.hunters.size > 1) {
            player.teleport(this.players.getPreviousLocation(player))
            this.removeHunter(player)
            thread(true) { HuntraceGamePlayerQuitEvent(this, player).callEvent() }
            true
        } else if (player in this.survivors && this.survivors.size > 1) {
            player.teleport(this.players.getPreviousLocation(player))
            this.removeSurvivor(player)
            thread(true) { HuntraceGamePlayerQuitEvent(this, player).callEvent() }
            true
        } else if (player in this.spectators) {
            this.removeSpectator(player)
            thread(true) { HuntraceGamePlayerQuitEvent(this, player).callEvent() }
            true
        } else {
            false
        }
    }

    /**
     * Turn given player to spectator
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
     * @exception IllegalStateException if player is the only hunter or survivor
     */
    @JvmName("-turnToSpectator")
    internal fun turnToSpectator(player: Player) {
        check(this.state == State.RUNNING) {
            "Can turn player to a spectator only when the io.craftray.huntrace.game is running"
        }

        if (player in this.hunters) {
            check(this.hunters.size != 1) {
                "Can't turn player to a spectator when there is only one hunter" // and that's time to finish the game
            }
            this.removeHunter(player)
            this.addSpectator(player)
        } else if (player in this.survivors) {
            check(this.survivors.size != 1) {
                "Can't turn player to a spectator when there is only one survivor" // ^
            }
            this.removeSurvivor(player)
            this.addSpectator(player)
        }

        this.turnGameModeTo(player)
    }

    /**
     * Add a hunter to the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to add
     * @exception IllegalStateException if the io.craftray.huntrace.game is started
     */
    @Throws(IllegalStateException::class)
    fun addHunter(player: Player) = this.players.addHunter(player)

    /**
     * Add a survivor to the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to add
     * @exception IllegalStateException if the io.craftray.huntrace.game is started
     */
    @Throws(IllegalStateException::class)
    fun addSurvivor(player: Player) = this.players.addSurvivor(player)

    /**
     * Add a spectator to the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to add
     * @exception IllegalStateException if the io.craftray.huntrace.game is started
     */
    @Throws(IllegalStateException::class)
    fun addSpectator(player: Player) = this.players.addSpectator(player)

    /**
     * remove a spectator from the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to remove
     * @exception IllegalStateException if the io.craftray.huntrace.game is started and they is the only online spectator
     */
    @Throws(IllegalStateException::class)
    fun removeSpectator(player: Player) = this.players.removeSpectator(player)

    /**
     * remove a hunter from the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to remove
     * @exception IllegalStateException if the game is started and they is the only online hunter
     */
    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        // otherwise, the compass will continue to track
        this.compassUpdater.stopTrackFor(player)
        this.players.removeHunter(player)
    }

    /**
     * Set the target of given hunter
     * @author Kylepoops
     * @param hunter the hunter to set the target of
     * @param target the target to set
     * @exception IllegalStateException if the player is not a hunter
     * @exception IllegalStateException if the game is not started
     * @exception IllegalArgumentException if the target is not a survivor
     */
    fun setTarget(hunter: Player, target: Player) {
        check(state == State.RUNNING) { "Can set target only on a running game" }
        check(hunter in hunters) { "player to set the target must be a hunter" }
        require(target in survivors) { "target must be a survivor" }
        this.hunterTargets.setTarget(hunter, target)
    }

    /**
     * remove a survivor from the io.craftray.huntrace.game
     * @author Kylepoops
     * @param player the hunter to remove
     * @exception IllegalStateException if the io.craftray.huntrace.game is started and they is the only online survivor
     */
    @Throws(IllegalStateException::class)
    fun removeSurvivor(player: Player) = this.players.removeSurvivor(player)

    /**
     * Teleport the player to their previous location
     */
    internal fun teleportFrom(player: Player) = player.teleport(players.getPreviousLocation(player))
    /**
     * teleport all players to the location before the io.craftray.huntrace.game start
     * @author Kylepoops
     */
    private fun teleportFrom() {
        this.survivors.forEach(::teleportFrom)
        this.hunters.forEach(::teleportFrom)
        this.spectators.forEach(::teleportFrom)
    }

    /**
     * teleport all player to the io.craftray.huntrace.game
     * @author Kylepoops
     */
    private fun teleportTo() {
        this.survivors.forEach { it.teleport(worlds.overworld.spawnLocation) }
        this.hunters.forEach { it.teleport(worlds.overworld.spawnLocation) }
        this.spectators.forEach { it.teleport(worlds.overworld.spawnLocation) }
    }

    /**
     * Turn given player gamemode
     * @author Kylepoops
     * @exception IllegalArgumentException if the player is not in the io.craftray.huntrace.game
     */
    @Throws(IllegalArgumentException::class)
    private fun turnGameModeTo(player: Player) = when (player) {
        in this.hunters -> player.gameMode = GameMode.SURVIVAL
        in this.survivors -> player.gameMode = GameMode.SURVIVAL
        in this.spectators -> player.gameMode = GameMode.SPECTATOR
        else -> throw IllegalArgumentException("Player is not in the io.craftray.huntrace.game")
    }

    /**
     * Turn gamemode for all players in the io.craftray.huntrace.game
     * @author Kylepoops
     */
    private fun turnGameModeTo() {
        this.hunters.forEach { this.turnGameModeTo(it) }
        this.survivors.forEach { this.turnGameModeTo(it) }
        this.spectators.forEach { this.turnGameModeTo(it) }
    }

    /**
     * Turn given player gamemode to original one (currently it can only turn to survival)
     * @author Kylepoops
     */
    private fun turnGameModeFrom(player: Player) {
        player.gameMode = GameMode.SURVIVAL
    }

    /**
     * Turn gamemode for all players in the io.craftray.huntrace.game to original one (currently it can only turn to survival)
     * @author Kylepoops
     */
    private fun turnGameModeFrom() {
        this.hunters.forEach { this.turnGameModeFrom(it) }
        this.survivors.forEach { this.turnGameModeFrom(it) }
        this.spectators.forEach { this.turnGameModeFrom(it) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (gameID != other.gameID) return false

        return true
    }

    override fun hashCode() = gameID.hashCode()

    enum class State {
        WAITING,
        INITIALIZED,
        PREPARING,
        RUNNING,
        FINISHED
    }

    companion object {
        val runningGame = InternalMutableSet<Game>()

        internal lateinit var plugin: Plugin

        /**
         * Init this before construct any game
         * @author Kylepoops
         * @param plugin the plugin instance
         */
        fun init(plugin: Plugin) {
            this.plugin = plugin
            MultiverseManager.initMultiverse()
        }

        /**
         * Find the io.craftray.huntrace.game by given player
         * @author Kylepoops
         * @param player the player to find
         * @return the io.craftray.huntrace.game or null if not found
         */
        fun findGameByPlayerOrNull(player: Player) = runningGame.find { it.players.contains(player) }

        /**
         * Get the io.craftray.huntrace.game by given player
         * @author Kylepoops
         * @param player the player to find
         * @return the io.craftray.huntrace.game
         * @exception IllegalArgumentException if the io.craftray.huntrace.game is not found
         */
        @Throws(IllegalArgumentException::class)
        fun getGameByPlayer(player: Player) = requireNotNull(findGameByPlayerOrNull(player)) { "This player is not in any game" }
    }
}