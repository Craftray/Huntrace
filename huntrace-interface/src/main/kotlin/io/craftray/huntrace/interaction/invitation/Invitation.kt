package io.craftray.huntrace.interaction.invitation

import org.bukkit.entity.Player

internal data class Invitation(
    val invitor: Player,
    val invitee: Player,
    val type: InvitationType,
    val timeMiles: Long = System.currentTimeMillis()
)

enum class InvitationType {
    SURVIVOR,
    HUNTER
}