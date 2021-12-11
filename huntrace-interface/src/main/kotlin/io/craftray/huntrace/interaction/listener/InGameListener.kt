package io.craftray.huntrace.interaction.listener

import io.craftray.huntrace.game.GameResult
import io.craftray.huntrace.game.event.HuntraceGameCompassUpdateEvent
import io.craftray.huntrace.game.event.HuntraceGameFinishEvent
import io.craftray.huntrace.game.event.HuntraceGameStartEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import taboolib.common.platform.event.SubscribeEvent

@Suppress("unused")
object InGameListener {
    @SubscribeEvent
    fun onGameStart(event: HuntraceGameStartEvent) {
        event.game.allPlayers.forEach {
            it.sendMessage(
                Component.text("[Huntrace] Game will be started in 10 seconds, ").color(NamedTextColor.DARK_AQUA)
                    .append(Component.text("Ready...").color(NamedTextColor.DARK_GRAY))
            )
        }
    }

    @SubscribeEvent
    fun onGameFinish(event: HuntraceGameFinishEvent) {
        val message = Component.text("[Huntrace] Game Finished: ")
        message.append(
            when (event.result) {
                GameResult.HUNTER_WIN -> Component.text("Hunter Win!").color(NamedTextColor.DARK_RED)
                GameResult.SURVIVOR_WIN -> Component.text("Survivor Win!").color(NamedTextColor.DARK_GREEN)
                GameResult.SURVIVOR_QUIT -> Component.text("Survivor Quit!").color(NamedTextColor.DARK_GREEN)
                GameResult.HUNTER_QUIT -> Component.text("Hunter Quit!").color(NamedTextColor.DARK_RED)
                GameResult.ABORT -> Component.text("Abort!").color(NamedTextColor.DARK_RED)
            }
        )

        event.game.allPlayers.forEach {
            it.sendMessage(message)
        }
    }

    @SubscribeEvent
    fun onCompassUpdate(event: HuntraceGameCompassUpdateEvent) {
        event.hunter.sendActionBar(
            Component.text("[Huntrace] You are now tracking").color(NamedTextColor.DARK_AQUA)
                .append(Component.text(event.target.name).color(NamedTextColor.DARK_GRAY))
        )
    }
}