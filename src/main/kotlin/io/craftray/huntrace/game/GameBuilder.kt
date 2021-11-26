package io.craftray.huntrace.game

import io.craftray.huntrace.rule.CompassRule
import io.craftray.huntrace.rule.RuleSet
import io.craftray.huntrace.rule.WorldRule
import org.bukkit.entity.Player
import kotlin.jvm.Throws

class GameBuilder {
    private lateinit var worldRule: WorldRule
    private lateinit var compassRule: CompassRule
    private lateinit var survivor: Player
    private val hunters = mutableSetOf<Player>()

    /**
     * Set the world rule.
     * @param rule the world rule
     * @exception IllegalStateException if the world rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: WorldRule) = this.apply {
        if (this::worldRule.isInitialized) {
            throw IllegalStateException("World rule already set")
        }
        this.worldRule = rule
    }

    /**
     * Set the compass rule
     * @param rule the compass rule
     * @exception IllegalStateException if the compass rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: CompassRule) = this.apply {
        if (this::compassRule.isInitialized) {
            throw IllegalStateException("Compass rule already set")
        }
        this.compassRule = rule
    }

    /**
     * Set both world rule and compass rule with a ruleset
     * @param ruleSet the ruleset
     * @exception IllegalStateException if either the world rule or compass rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRules(rules: RuleSet) = this.apply {
        this.withRule(rules.worldRule).withRule(rules.compassRule)
    }

    /**
     * Set the survivor of the game
     * @param survivor the survivor
     * @exception IllegalStateException if the survivor has already been set
     */
    @Throws(IllegalStateException::class)
    fun withSurvivor(survivor: Player) = this.apply {
        if (this::survivor.isInitialized) {
            throw IllegalStateException("Survivor already set")
        }
        this.survivor = survivor
    }

    /**
     * Add a hunter to the game
     * @param hunter the hunter
     */
    fun withHunter(hunter: Player) = this.apply {
        this.hunters.add(hunter)
    }

    /**
     * Add a list of hunters to the game
     * @param hunters the hunters
     */
    fun withHunters(hunters: Collection<Player>) = this.apply {
        this.hunters.addAll(hunters)
    }

    /**
     * Add a list of hunters to the game
     * @param hunters the hunters
     */
    fun withHunters(vararg hunters: Player) = this.apply {
        this.hunters.addAll(hunters)
    }

    /**
     * Build a game with given values
     * @exception IllegalStateException if the world rule, compass rule, or survivor has not been set
     * @return the game
     */
    @Throws(IllegalStateException::class)
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