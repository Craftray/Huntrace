package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

class HuntraceGameListener(val game: Game) : Listener {
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity == game.players.getSurvivor()) game.finish(GameResult.HUNTER_WIN)
    }

    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player == game.players.getSurvivor()) game.finish(GameResult.SURVIVOR_QUIT)
    }
}