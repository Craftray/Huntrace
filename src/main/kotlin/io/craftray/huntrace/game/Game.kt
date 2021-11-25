package io.craftray.huntrace.game

import io.craftray.huntrace.game.compass.CompassUpdater
import io.craftray.huntrace.multiverse.MultiverseWorldManager.toMVWorld
import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.world.WorldController.deleteWorlds
import io.craftray.huntrace.world.WorldController.generateWorlds
import io.craftray.huntrace.world.WorldController.linkWorlds
import io.craftray.huntrace.world.WorldController.unlinkWorlds
import io.craftray.huntrace.world.WorldSet
import kotlin.properties.Delegates

class Game(val rules: RuleSet) {
    val gameID = java.util.UUID.randomUUID()!!

    private val compassUpdater = CompassUpdater(this)

    lateinit var worlds: WorldSet
        private set

    val players = PlayerSet()

    var startTime by Delegates.notNull<Long>()
    var endTime by Delegates.notNull<Long>()

    fun init() {
        players.lock()
        runningGame.add(this)
        this.compassUpdater.init()
        this.generateWorlds()
        this.linkWorlds()
        this.teleportTo()
        this.startTime = System.currentTimeMillis()
    }

    fun finish(result: GameResult) {
        this.teleportFrom()
        this.matchResult(result)
        this.compassUpdater.stop()
        this.unlinkWorlds()
        this.deleteWorlds()
        this.endTime = System.currentTimeMillis()
        runningGame.remove(this)
    }

    fun matchResult(result: GameResult) = when(result) {
        GameResult.HUNTER_WIN -> this.hunterWin()
        GameResult.SURVIVOR_WIN -> this.survivorWin()
        GameResult.SURVIVOR_QUIT -> this.survivorQuit()
    }

    fun hunterWin() {}
    fun survivorWin() {}
    fun survivorQuit() {}

    fun teleportTo() {
        players.getSurvivor().teleport(worlds.overworld.spawnLocation)
        players.getHunters().forEach { it.teleport(worlds.overworld.spawnLocation) }
    }

    fun teleportFrom() {
        players.getSurvivor().let { it.teleport(players.getPreviousLocation(it)) }
        players.getHunters().forEach { it.teleport(players.getPreviousLocation(it)) }
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