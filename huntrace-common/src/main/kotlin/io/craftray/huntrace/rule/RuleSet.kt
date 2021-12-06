package io.craftray.huntrace.rule

import java.io.Serializable

open class RuleSet(
    open var worldRule: WorldRule,
    open var compassRule: CompassRule
) : Serializable {
    override fun toString() = "RuleSet(worldRule=$worldRule, compassRule=$compassRule)"

    /**
     * Make a copy of this rule set
     * @author Kylepoops
     * @return the copy
     */
    fun copy() = RuleSet(worldRule.copy(), compassRule.copy())

    /**
     * Make a immutable copy of this rule set
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

@Suppress("UseCheckOrError")
class ImmutableRuleSet(
    worldRule: WorldRule,
    compassRule: CompassRule
) : RuleSet(worldRule, compassRule) {
    override var worldRule: WorldRule
        get() = super.worldRule
        set(value) {
            throw IllegalStateException("Cannot change immutable rule set")
        }

    override var compassRule: CompassRule
        get() = super.compassRule
        set(value) {
            throw IllegalStateException("Cannot change immutable rule set")
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