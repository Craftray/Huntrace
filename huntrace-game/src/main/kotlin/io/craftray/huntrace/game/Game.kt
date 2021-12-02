package io.craftray.huntrace.game

import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.collection.HunterTargetCollection
import io.craftray.huntrace.game.collection.PlayerDataCollection
import io.craftray.huntrace.game.collection.WorldCollection
import io.craftray.huntrace.game.event.HuntraceGameFinishEvent
import io.craftray.huntrace.game.event.HuntraceGameHunterQuitEvent
import io.craftray.huntrace.game.event.HuntraceGameStartEvent
import io.craftray.huntrace.game.listener.HuntraceGameMainListener
import io.craftray.huntrace.game.listener.HuntraceGamePrepareStateListener
import io.craftray.huntrace.game.multiverse.MultiverseManager
import io.craftray.huntrace.game.schedular.CompassUpdater
import io.craftray.huntrace.rule.RuleSet
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
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

    /**
     * Initialize a io.craftray.huntrace.game
     * Generate three dimension, setup CompassUpdater, GameWorldController and GameResultMatcher
     * @author Kylepoops
     */
    fun init() {
        this.rules = this.rules.immutableCopy()
        this.compassUpdater = CompassUpdater(this)
        this.compassUpdater = CompassUpdater(this)
        this.worldController = GameWorldController(this)
        this.mainListener = HuntraceGameMainListener(this).also { it.register() }
        this.worldController.generateWorlds()
        this.worldController.linkWorlds()
        this.state = State.INITIALIZED
    }

    /**
     * Start an initialized io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not initialized or already started
     */
    @Throws(IllegalStateException::class)
    fun start() {
        if (this.state != State.INITIALIZED) throw IllegalStateException("Game is not initialized")
        this.players.lock()
        this.players.storeLocation()
        runningGame.add(this)
        this.teleportTo()
        this.turnGameModeTo()
        this.compassUpdater.start()
        this.startTime = System.currentTimeMillis()
        HuntraceGameStartEvent(this).callEvent()
        this.state = State.PREPARING
        this.prepare()
    }

    /**
     * Prepare a io.craftray.huntrace.game and turn it to running state after 10s
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not preparing
     */
    private fun prepare() {
        if (this.state != State.PREPARING) throw IllegalStateException("Game is not preparing")
        this.prepareListener = HuntraceGamePrepareStateListener(this).also { it.register() }
        bukkitRunnableOf {
            this.prepareListener.unregister()
            this.state = State.RUNNING
        }.runTaskLater(plugin, 200L)
    }

    /**
     * Finish a io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
     */
    fun finish(result: GameResult) {
        if (this.state != State.RUNNING) throw IllegalStateException("Game is not started")
        this.mainListener.unregister()
        this.turnGameModeFrom()
        this.teleportFrom()
        this.compassUpdater.stop()
        this.worldController.unlinkWorlds()
        this.worldController.deleteWorlds()
        this.endTime = System.currentTimeMillis()
        runningGame.remove(this)
        HuntraceGameFinishEvent(this, result).callEvent()
        this.state = State.FINISHED
    }

    /**
     * Abort the io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
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
        if (this.state != State.RUNNING) throw IllegalStateException("Game is not started")

        if (player in this.survivors && this.survivors.size <= 1) {
            this.finish(GameResult.SURVIVOR_QUIT)
            return true
        } else if (player in this.hunters && this.hunters.size <= 1) {
            this.finish(GameResult.HUNTER_QUIT)
            return true
        } else if (player in this.hunters && this.hunters.size > 1) {
            player.teleport(this.players.getPreviousLocation(player))
            this.removeHunter(player)
            HuntraceGameHunterQuitEvent(this, player).callEvent()
            return true
        } else if (player in this.survivors && this.survivors.size > 1) {
            player.teleport(this.players.getPreviousLocation(player))
            this.removeSurvivor(player)
            HuntraceGameHunterQuitEvent(this, player).callEvent()
            return true
        }

        return false
    }

    /**
     * Turn given player to spectator
     * @author Kylepoops
     * @exception IllegalStateException if the io.craftray.huntrace.game is not started
     * @exception IllegalStateException if player is the only hunter or survivor
     */
    fun turnToSpectator(player: Player) {
        if (this.state != State.RUNNING) {
            throw IllegalStateException("Can turn player to a spectator only when the io.craftray.huntrace.game is running")
        }

        if (player in this.hunters) {
            if (this.hunters.size == 1) {
                throw IllegalStateException("Can't turn player to a spectator when there is only one hunter")
            }
            this.removeHunter(player)
            this.addSpectator(player)
        } else if (player in this.survivors) {
            if (this.survivors.size == 1) {
                throw IllegalStateException("Can't turn player to a spectator when there is only one survivor")
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
     * @exception IllegalStateException if the io.craftray.huntrace.game is started and they is the only online hunter
     */
    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        this.compassUpdater.stopTrackFor(player)
        this.players.removeHunter(player)
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
     * teleport all players to the location before the io.craftray.huntrace.game start
     * @author Kylepoops
     */
    private fun teleportFrom() {
        this.survivors.forEach { it.teleport(players.getPreviousLocation(it)) }
        this.hunters.forEach { it.teleport(players.getPreviousLocation(it)) }
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
    private fun turnGameModeTo(player: Player) = when(player) {
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

    override fun hashCode(): Int {
        return gameID.hashCode()
    }

    enum class State {
        WAITING,
        INITIALIZED,
        PREPARING,
        RUNNING,
        FINISHED
    }

    companion object {
        val runningGame = mutableSetOf<Game>()
        
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
        fun getGameByPlayer(player: Player): Game {
            return findGameByPlayerOrNull(player) ?: throw IllegalArgumentException("This player is not in any io.craftray.huntrace.game")
        }
    }
}