package io.craftray.huntrace.interaction.invitation

import org.bukkit.entity.Player

/**
 * Represents an invitation to join a game.
 * @param invitor The player who invited the other player.
 * @param invitee The player who was invited.
 * @param type The type of invitation.
 * @param timeMiles the time when the invitation is constructed
 */
internal data class Invitation(
    val invitor: Player,
    val invitee: Player,
    val type: InvitationType,
    val timeMiles: Long = System.currentTimeMillis()
)

/**
 * Type of invitation
 */
enum class InvitationType {
    /**
     * Invite as a survivor
     */
    SURVIVOR,

    /**
     * Invite as a hunter
     */
    HUNTER
}