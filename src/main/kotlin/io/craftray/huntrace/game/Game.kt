package io.craftray.huntrace.game

import io.craftray.huntrace.game.collection.PlayerSet
import io.craftray.huntrace.game.collection.WorldSet
import io.craftray.huntrace.game.event.HuntraceGameFinishEvent
import io.craftray.huntrace.game.event.HuntraceGameHunterQuitEvent
import io.craftray.huntrace.game.event.HuntraceGameStartEvent
import io.craftray.huntrace.game.listener.HuntraceGameListener
import io.craftray.huntrace.game.schedular.CompassUpdater
import io.craftray.huntrace.rule.RuleSet
import org.bukkit.entity.Player
import java.util.*
import kotlin.properties.Delegates

class Game(rules: RuleSet) {
    val gameID = UUID.randomUUID()!!
    private lateinit var compassUpdater: CompassUpdater
    private lateinit var worldController: GameWorldController
    private lateinit var listener: HuntraceGameListener
    private val players = PlayerSet()
    private val resultMatcher = GameResultMatcher(this)
    private var initialized = false
    private var started = false

    var startTime by Delegates.notNull<Long>()
        private set
    var endTime by Delegates.notNull<Long>()
        private set
    lateinit var worlds: WorldSet
        private set
    var rules = rules
        private set

    var survivor
        get() = this.players.survivor
        set(value) { this.players.survivor = value }

    val hunters
        get() = this.players.hunters

    /**
     * Initialize a game
     * Generate three dimension, setup CompassUpdater, GameWorldController and GameResultMatcher
     * @author Kylepoops
     */
    fun init() {
        this.rules = this.rules.immutableCopy()
        this.compassUpdater = CompassUpdater(this)
        this.worldController = GameWorldController(this)
        this.listener = HuntraceGameListener(this).also { it.register() }
        this.worldController.generateWorlds()
        this.worldController.linkWorlds()
        this.initialized = true
    }

    /**
     * Start a initialized game
     * @author Kylepoops
     * @exception IllegalStateException if the game is not initialized or already started
     */
    @Throws(IllegalStateException::class)
    fun start() {
        if (!this.initialized) throw IllegalStateException("Game is not initialized")
        if (this.started) throw IllegalStateException("Game is already started")
        this.players.lock()
        this.players.storeLocation()
        runningGame.add(this)
        this.teleportTo()
        this.compassUpdater.start()
        this.startTime = System.currentTimeMillis()
        HuntraceGameStartEvent(this).callEvent()
    }

    /**
     * Finish a game
     * @author Kylepoops
     * @exception IllegalStateException if the game is not started
     */
    fun finish(result: GameResult) {
        if (!this.started) throw IllegalStateException("Game is not started")
        this.listener.unregister()
        this.teleportFrom()
        this.resultMatcher.match(result)
        this.compassUpdater.stop()
        this.worldController.unlinkWorlds()
        this.worldController.deleteWorlds()
        this.endTime = System.currentTimeMillis()
        runningGame.remove(this)
        HuntraceGameFinishEvent(this, result).callEvent()
    }

    /**
     * Abort the game
     * @author Kylepoops
     * @exception IllegalStateException if the game is not started
     */
    @Throws(IllegalStateException::class)
    fun abort() = finish(GameResult.ABORT)

    /**
     * Make a player quit the game
     * @author Kylepoops
     * @param player the player to quit
     * @exception IllegalStateException if the game is not started
     */
    @Throws(IllegalStateException::class)
    fun quit(player: Player): Boolean {
        if (!this.started) throw IllegalStateException("Game is not started")

        if (this.survivor == player) {
            this.finish(GameResult.SURVIVOR_QUIT)
            return true
        } else if (this.hunters.contains(player) && this.hunters.size <= 1) {
            this.finish(GameResult.HUNTER_QUIT)
            return true
        } else if (this.hunters.contains(player) && this.hunters.size > 1) {
            player.teleport(this.players.getPreviousLocation(player))
            this.removeHunter(player)
            HuntraceGameHunterQuitEvent(this, player).callEvent()
            return true
        }

        return false
    }

    /**
     * Add a hunter to the game
     * @author Kylepoops
     * @param player the hunter to add
     * @exception IllegalStateException if the game is started
     */
    @Throws(IllegalStateException::class)
    fun addHunter(player: Player) = this.players.addHunter(player)

    /**
     * remove a hunter from the game
     * @author Kylepoops
     * @param player the hunter to remove
     * @exception IllegalStateException if the game is started and the hunter is still online
     */
    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) = this.players.removeHunter(player)

    /**
     * teleport all players to the location before the game start
     * @author Kylepoops
     */
    private fun teleportFrom() {
        this.survivor.let { it.teleport(players.getPreviousLocation(it)) }
        this.hunters.forEach { it.teleport(players.getPreviousLocation(it)) }
    }

    /**
     * teleport all player to the game
     * @author Kylepoops
     */
    private fun teleportTo() {
        this.survivor.teleport(worlds.overworld.spawnLocation)
        this.hunters.forEach { it.teleport(worlds.overworld.spawnLocation) }
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

    companion object {
        val runningGame = mutableSetOf<Game>()

        /**
         * Find the game by given player
         * @author Kylepoops
         * @param player the player to find
         * @return the game or null if not found
         */
        fun findGameByPlayerOrNull(player: Player) = runningGame.find { it.players.contains(player) }

        /**
         * Get the game by given player
         * @author Kylepoops
         * @param player the player to find
         * @return the game
         * @exception IllegalArgumentException if the game is not found
         */
        @Throws(IllegalArgumentException::class)
        fun getGameByPlayer(player: Player): Game {
            return findGameByPlayerOrNull(player) ?: throw IllegalArgumentException("This player is not in any game")
        }
    }
}