package io.craftray.huntrace.game

import io.craftray.huntrace.game.collection.PlayerSet
import io.craftray.huntrace.game.collection.WorldSet
import io.craftray.huntrace.game.listener.HuntraceGameListener
import io.craftray.huntrace.game.schedular.CompassUpdater
import io.craftray.huntrace.rule.RuleSet
import org.bukkit.entity.Player
import java.util.*
import kotlin.properties.Delegates

class Game(rules: RuleSet) {
    val gameID = UUID.randomUUID()!!
    private val compassUpdater = CompassUpdater(this)
    private lateinit var listener: HuntraceGameListener
    private val players = PlayerSet()
    private val resultMatcher = GameResultMatcher(this)
    private val worldController = GameWorldController(this)
    var startTime by Delegates.notNull<Long>()
    var endTime by Delegates.notNull<Long>()
    lateinit var worlds: WorldSet
        private set

    var rules = rules
        private set

    var survivor
        get() = this.players.survivor
        set(value) { this.players.survivor = value }

    val hunters
        get() = this.players.hunters

    fun init() {
        this.rules = this.rules.immutableCopy()
        this.listener = HuntraceGameListener(this).also { it.register() }
        this.worldController.generateWorlds()
        this.worldController.linkWorlds()
    }

    fun start() {
        this.players.lock()
        this.players.storeLocation()
        runningGame.add(this)
        this.teleportTo()
        this.compassUpdater.init()
        this.startTime = System.currentTimeMillis()
    }

    fun finish(result: GameResult) {
        this.listener.unregister()
        this.teleportFrom()
        this.resultMatcher.match(result)
        this.compassUpdater.stop()
        this.worldController.unlinkWorlds()
        this.worldController.deleteWorlds()
        this.endTime = System.currentTimeMillis()
        runningGame.remove(this)
    }

    fun addHunter(player: Player) = this.players.addHunter(player)

    fun removeHunter(player: Player) = this.players.removeHunter(player)

    private fun teleportFrom() {
        this.survivor.let { it.teleport(players.getPreviousLocation(it)) }
        this.hunters.forEach { it.teleport(players.getPreviousLocation(it)) }
    }

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
    }
}