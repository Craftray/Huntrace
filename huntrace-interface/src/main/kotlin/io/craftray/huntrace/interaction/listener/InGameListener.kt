package io.craftray.huntrace.interaction.listener

import io.craftray.huntrace.game.event.HuntraceGameStartEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent

object InGameListener {
    @SubscribeEvent
    fun onGameStart(event: HuntraceGameStartEvent) {
        Bukkit.broadcast(Component.text(event.game.survivors.joinToString() + event.game.hunters.joinToString()))
    }
}