package io.craftray.huntrace.interaction

import io.craftray.huntrace.interaction.invitation.InvitationType
import io.craftray.huntrace.rule.CompassRule
import io.craftray.huntrace.rule.WorldRule
import org.bukkit.entity.Player

internal data class GameSetting(
    var hunters: MutableSet<Player> = mutableSetOf(),
    var survivors: MutableSet<Player> = mutableSetOf(),
    var compassRule: CompassRule = CompassRule(),
    var worldRule: WorldRule = WorldRule()
) {
    fun addPlayer(player: Player, type: InvitationType) {
        when (type) {
            InvitationType.HUNTER -> hunters.add(player)
            InvitationType.SURVIVOR -> survivors.add(player)
        }
    }
}