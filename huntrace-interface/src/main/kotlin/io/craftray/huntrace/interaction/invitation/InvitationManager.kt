package io.craftray.huntrace.interaction.invitation

import org.bukkit.entity.Player

internal object InvitationManager {
    val invitations = mutableSetOf<Invitation>()

    fun get(invitor: Player, invitee: Player) = invitations.find { it.invitor == invitor && it.invitee == invitee }

    fun contains(invitor: Player, invitee: Player) = invitations.any { it.invitor == invitor && it.invitee == invitee }

    fun remove(invitor: Player, invitee: Player) = invitations.removeIf { it.invitor == invitor && it.invitee == invitee }
}