package io.craftray.huntrace.game

import io.craftray.huntrace.util.rule.CompassRule
import io.craftray.huntrace.util.rule.RuleSet
import io.craftray.huntrace.util.rule.WorldRule
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import org.bukkit.entity.Player
import kotlin.jvm.Throws

@Suppress("unused")
class GameBuilder {
    private lateinit var worldRule: WorldRule
    private lateinit var compassRule: CompassRule
    private val survivors = ObjectLinkedOpenHashSet<Player>()
    private val hunters = ObjectLinkedOpenHashSet<Player>()

    /**
     * Set the world rule.
     * @param rule the world rule
     * @exception IllegalStateException if the world rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: WorldRule) = this.apply {
        check(!this::worldRule.isInitialized) { "World rule already set" }
        this.worldRule = rule
    }

    /**
     * Set the compass rule
     * @param rule the compass rule
     * @exception IllegalStateException if the compass rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: CompassRule) = this.apply {
        check(!this::compassRule.isInitialized) { "Compass rule already set" }
        this.compassRule = rule
    }

    /**
     * Set both world rule and compass rule with a ruleset
     * @param rules the ruleset
     * @exception IllegalStateException if either the world rule or compass rule has already been set
     */
    @Throws(IllegalStateException::class)
    fun withRules(rules: RuleSet) = this.apply {
        this.withRule(rules.worldRule).withRule(rules.compassRule)
    }

    /**
     * Set the survivor of the io.craftray.huntrace.game
     * @param survivor the survivor
     */
    @Throws(IllegalStateException::class)
    fun withSurvivor(survivor: Player) = this.apply {
        this.survivors.add(survivor)
    }

    /**
     * Add a list of survivor to the io.craftray.huntrace.game
     * @param survivors the hunters
     */
    fun withSurvivors(survivors: Collection<Player>) = this.apply {
        this.survivors.addAll(survivors)
    }

    /**
     * Add a list of hunters to the io.craftray.huntrace.game
     * @param survivors the hunters
     */
    fun withSurvivors(vararg survivors: Player) = this.apply {
        this.hunters.addAll(survivors)
    }

    /**
     * Add a hunter to the io.craftray.huntrace.game
     * @param hunter the hunter
     */
    fun withHunter(hunter: Player) = this.apply {
        this.hunters.add(hunter)
    }

    /**
     * Add a list of hunters to the io.craftray.huntrace.game
     * @param hunters the hunters
     */
    fun withHunters(hunters: Collection<Player>) = this.apply {
        this.hunters.addAll(hunters)
    }

    /**
     * Add a list of hunters to the io.craftray.huntrace.game
     * @param hunters the hunters
     */
    fun withHunters(vararg hunters: Player) = this.apply {
        this.hunters.addAll(hunters)
    }

    /**
     * Build a game with given values
     * @exception IllegalStateException if the builder is not fully initialized
     * @return the io.craftray.huntrace.game
     */
    @Throws(IllegalStateException::class)
    fun build(): Game {
        val noneEmpty = this.survivors.isNotEmpty() && this.hunters.isNotEmpty()
        val allInitialized = this::worldRule.isInitialized && this::compassRule.isInitialized
        check(noneEmpty && allInitialized) { "GameBuilder is not fully initialized" }
        val rules = RuleSet(worldRule, compassRule)
        return Game(rules).apply {
            this@GameBuilder.survivors.forEach { this.addSurvivor(it) }
            this@GameBuilder.hunters.forEach { this.addHunter(it) }
        }
    }
}