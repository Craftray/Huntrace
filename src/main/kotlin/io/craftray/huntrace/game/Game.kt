package io.craftray.huntrace.game

import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.world.WorldController.deleteWorlds
import io.craftray.huntrace.world.WorldController.generateWorlds
import io.craftray.huntrace.world.WorldController.linkWorlds
import io.craftray.huntrace.world.WorldController.unlinkWorlds
import io.craftray.huntrace.world.WorldSet
import kotlin.properties.Delegates

class Game(val rules: RuleSet) {
    val gameID = java.util.UUID.randomUUID()!!

    lateinit var worlds: WorldSet
        private set

    val players = PlayerSet()

    var startTime by Delegates.notNull<Long>()
    var endTime by Delegates.notNull<Long>()

    fun init() {
        this.generateWorlds()
        this.linkWorlds()
        this.startTime = System.currentTimeMillis()
    }

    fun finish() {
        this.unlinkWorlds()
        this.deleteWorlds()
        this.endTime = System.currentTimeMillis()
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
}