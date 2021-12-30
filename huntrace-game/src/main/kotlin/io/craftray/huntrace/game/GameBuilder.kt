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
     * @return this builder for chaining calls
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: WorldRule): GameBuilder {
        check(!this::worldRule.isInitialized) { "World rule already set" }
        this.worldRule = rule
        return this
    }

    /**
     * Set the compass rule
     * @param rule the compass rule
     * @exception IllegalStateException if the compass rule has already been set
     * @return this builder for chaining calls
     */
    @Throws(IllegalStateException::class)
    fun withRule(rule: CompassRule): GameBuilder {
        check(!this::compassRule.isInitialized) { "Compass rule already set" }
        this.compassRule = rule
        return this
    }

    /**
     * Set both world rule and compass rule with a ruleset
     * @param rules the ruleset
     * @exception IllegalStateException if either the world rule or compass rule has already been set
     * @return this builder for chaining calls
     */
    @Throws(IllegalStateException::class)
    fun withRules(rules: RuleSet): GameBuilder {
        this.withRule(rules.worldRule).withRule(rules.compassRule)
        return this
    }

    /**
     * Set the survivor of the game
     * @param survivor the survivor
     * @return this builder for chaining calls
     */
    @Throws(IllegalStateException::class)
    fun withSurvivor(survivor: Player): GameBuilder {
        this.survivors.add(survivor)
        return this
    }

    /**
     * Add a list of survivor to the game
     * @param survivors the hunters
     * @return this builder for chaining calls
     */
    fun withSurvivors(survivors: Collection<Player>): GameBuilder {
        this.survivors.addAll(survivors)
        return this
    }

    /**
     * Add a list of hunters to the game
     * @param survivors the hunters
     * @return this builder for chaining calls
     */
    fun withSurvivors(vararg survivors: Player): GameBuilder {
        this.hunters.addAll(survivors)
        return this
    }

    /**
     * Add a hunter to the game
     * @param hunter the hunter
     * @return this builder for chaining calls
     */
    fun withHunter(hunter: Player): GameBuilder {
        this.hunters.add(hunter)
        return this
    }

    /**
     * Add a list of hunters to the game
     * @param hunters the hunters
     * @return this builder for chaining calls
     */
    fun withHunters(hunters: Collection<Player>): GameBuilder {
        this.hunters.addAll(hunters)
        return this
    }

    /**
     * Add a list of hunters to the game
     * @param hunters the hunters
     * @return this builder for chaining calls
     */
    fun withHunters(vararg hunters: Player): GameBuilder {
        this.hunters.addAll(hunters)
        return this
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