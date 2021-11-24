package io.craftray.huntrace

import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.world.WorldController.generateWorlds
import io.craftray.huntrace.world.WorldSet
import kotlin.properties.Delegates

class Game(val rules: RuleSet) {
    val gameID = java.util.UUID.randomUUID()!!

    lateinit var worlds: WorldSet
        private set

    var startTime by Delegates.notNull<Long>()

    fun init() {
        this.generateWorlds()
        this.startTime = System.currentTimeMillis()
    }

    fun finish() {

    }
}