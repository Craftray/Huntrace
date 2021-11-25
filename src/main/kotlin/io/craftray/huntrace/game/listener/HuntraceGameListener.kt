package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.*
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

class HuntraceGameListener(val game: Game) : Listener {
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity == game.survivor) game.finish(GameResult.HUNTER_WIN)
    }

    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player == game.survivor) game.finish(GameResult.SURVIVOR_QUIT)
        if (event.player in game.hunters) game.removeHunter(event.player)
        if (game.hunters.isEmpty()) game.finish(GameResult.HUNTER_QUIT)
    }
}