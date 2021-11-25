package io.craftray.huntrace.game.listener

import io.craftray.huntrace.Main
import io.craftray.huntrace.game.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

class HuntraceGameListener(private val game: Game) : Listener {

    fun register() {
        Bukkit.getPluginManager().registerEvents(this, Main.plugin)
    }

    fun unregister() {
        PlayerDeathEvent.getHandlerList().unregister(this)
        PlayerQuitEvent.getHandlerList().unregister(this)
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity == game.survivor) game.finish(GameResult.HUNTER_WIN)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player == game.survivor) game.finish(GameResult.SURVIVOR_QUIT)
        if (event.player in game.hunters) game.removeHunter(event.player)
        if (game.hunters.isEmpty()) game.finish(GameResult.HUNTER_QUIT)
    }
}