package io.craftray.huntrace.interaction.invitation

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import org.bukkit.entity.Player

/**
 * A delegate of MutableSet<Invitation> for handling invitations.
 */
internal object InvitationManager {

    val invitations = ObjectLinkedOpenHashSet<Invitation>()

    /**
     * Get the invitations of certain invitor and invitee
     * @param invitor the invitor
     * @param invitee the invitee
     * @return the invitation
     */
    fun get(invitor: Player, invitee: Player) = invitations.find { it.invitor == invitor && it.invitee == invitee }

    /**
     * Check if there is an invitation between two players
     * @param invitor the invitor
     * @param invitee the invitee
     * @return true if there is an invitation
     */
    fun contains(invitor: Player, invitee: Player) = invitations.any { it.invitor == invitor && it.invitee == invitee }

    /**
     * Remove an invitation between two players
     * @param invitor the invitor
     * @param invitee the invitee
     */
    fun remove(invitor: Player, invitee: Player) = invitations.removeIf { it.invitor == invitor && it.invitee == invitee }
}