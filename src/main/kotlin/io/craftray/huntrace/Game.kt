package io.craftray.huntrace

import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.world.WorldSet

class Game(val rules: RuleSet) {
    val worlds: WorldSet = WorldSet()

    fun generateWorlds() = Unit
}