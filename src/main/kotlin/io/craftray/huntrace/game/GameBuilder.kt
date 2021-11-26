package io.craftray.huntrace.game

import io.craftray.huntrace.rule.CompassRule
import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.rule.WorldRule
import org.bukkit.entity.Player

class GameBuilder {
    private lateinit var worldRule: WorldRule
    private lateinit var compassRule: CompassRule
    private lateinit var survivor: Player
    private val hunters = mutableSetOf<Player>()

    fun withRule(rule: WorldRule) {
        if (this::worldRule.isInitialized) {
            throw IllegalStateException("World rule already set")
        }
        this.worldRule = rule
    }

    fun withRule(rule: CompassRule) {
        if (this::compassRule.isInitialized) {
            throw IllegalStateException("Compass rule already set")
        }
        this.compassRule = rule
    }

    fun withRules(rules: RuleSet) {
        this.withRule(rules.worldRule)
        this.withRule(rules.compassRule)
    }

    fun withSurvivor(survivor: Player) {
        if (this::survivor.isInitialized) {
            throw IllegalStateException("Survivor already set")
        }
        this.survivor = survivor
    }

    fun withHunter(hunter: Player) {
        this.hunters.add(hunter)
    }

    fun withHunters(hunters: Collection<Player>) {
        this.hunters.addAll(hunters)
    }

    fun withHunters(vararg hunters: Player) {
        this.hunters.addAll(hunters)
    }

    fun build(): Game {
        if (!this::survivor.isInitialized || this.hunters.isEmpty() || !this::worldRule.isInitialized || this::compassRule.isInitialized) {
            throw IllegalStateException("GameBuilder is not fully initialized")
        }
        val rules = RuleSet(worldRule, compassRule)
        return Game(rules).apply {
            this.survivor = this@GameBuilder.survivor
            this@GameBuilder.hunters.forEach { this.addHunter(it) }
        }
    }

}