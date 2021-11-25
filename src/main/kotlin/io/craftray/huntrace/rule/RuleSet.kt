package io.craftray.huntrace.rule

import java.io.Serializable

data class RuleSet(
    var worldRule: WorldRule,
    var CompassRule: CompassRule
) : Serializable