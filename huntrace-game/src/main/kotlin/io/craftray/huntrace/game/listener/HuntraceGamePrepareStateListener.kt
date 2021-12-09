package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.Game
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class HuntraceGamePrepareStateListener(private val game: Game) : HuntraceGameListener() {
    /**
     * Prevent players from moving during preparing state
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.player.world in game.worlds) {
            event.isCancelled = true
        }
    }

    /**
     * Prevent players from interacting during preparing state
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.player.world in game.worlds) {
            event.isCancelled = true
        }
    }
}