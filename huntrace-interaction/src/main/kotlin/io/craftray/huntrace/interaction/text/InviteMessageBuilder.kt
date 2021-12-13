package io.craftray.huntrace.interaction.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

object InviteMessageBuilder {
    /**
     * construct a component of invitation message
     * @param invitor the invitor of the invitation
     * @return the component of invitation message
     */
    fun of(invitor: Player): Component {
        return Component.text("[Huntrace]").color(NamedTextColor.YELLOW).append(
            Component.text(" ${invitor.name} invited you to join their game!").color(NamedTextColor.DARK_GRAY)
        ).append(
            Component.newline()
        ).append(
            Component.text("[Accept]").color(NamedTextColor.GREEN).hoverEvent(
                Component.text("Click to accept").color(NamedTextColor.GREEN)
            ).clickEvent(
                ClickEvent.runCommand("/huntrace invite accept ${invitor.name}")
            )
        ).append(
            Component.text("  ")
        ).append(
            Component.text("[Deny]").color(NamedTextColor.RED).hoverEvent(
                Component.text("Click to deny").color(NamedTextColor.RED)
            ).clickEvent(
                ClickEvent.runCommand("/huntrace invite deny ${invitor.name}")
            )
        )
    }
}