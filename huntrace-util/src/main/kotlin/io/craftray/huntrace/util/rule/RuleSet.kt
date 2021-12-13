@file:Suppress("unused")

package io.craftray.huntrace.util.rule

import java.io.Serializable

/**
 * Mutable rule set for configuring a game.
 * @param worldRule the world rule
 * @param compassRule the compass rule
 */
open class RuleSet(
    open var worldRule: WorldRule = WorldRule(),
    open var compassRule: CompassRule = CompassRule()
) : Serializable {
    override fun toString() = "RuleSet(worldRule=$worldRule, compassRule=$compassRule)"

    /**
     * Make a copy of this rule set
     * @author Kylepoops
     * @return the copy
     */
    fun copy() = RuleSet(worldRule.copy(), compassRule.copy())

    /**
     * Make an immutable copy of this rule set to prevent accidental changes
     * @author Kylepoops
     * @return the copy
     */
    fun immutableCopy() = ImmutableRuleSet(worldRule.copy(), compassRule.copy())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleSet

        if (worldRule != other.worldRule) return false
        if (compassRule != other.compassRule) return false

        return true
    }

    override fun hashCode(): Int {
        var result = worldRule.hashCode()
        result = 31 * result + compassRule.hashCode()
        return result
    }

    companion object {
        const val serialVersionUID = 3248819797299803L
    }
}

/**
 * Immutable rule set for preventing changes during the game is running
 * @param worldRule the world rule
 * @param compassRule the compass rule
 */
class ImmutableRuleSet(
    worldRule: WorldRule,
    compassRule: CompassRule
) : RuleSet(worldRule, compassRule) {
    override var worldRule: WorldRule
        get() = super.worldRule
        set(@Suppress("UNUSED_PARAMETER") value) {
            error("Cannot change immutable rule set")
        }

    override var compassRule: CompassRule
        get() = super.compassRule
        set(@Suppress("UNUSED_PARAMETER") value) {
            error("Cannot change immutable rule set")
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ImmutableRuleSet

        if (worldRule != other.worldRule) return false
        if (compassRule != other.compassRule) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + worldRule.hashCode()
        result = 31 * result + compassRule.hashCode()
        return result
    }

    companion object {
        const val serialVersionUID = 8694221822079271L
    }
}