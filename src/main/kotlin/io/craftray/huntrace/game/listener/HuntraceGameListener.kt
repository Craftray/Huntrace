package io.craftray.huntrace.game.listener

import io.craftray.huntrace.Main
import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class HuntraceGameListener(private val game: Game) : Listener {

    private var registered = false
    private var unregistered = false

    /**
     * Register this listener
     * @author Kylepoops
     * @exception IllegalStateException if this listener is already registered
     */
    fun register() {
        if (registered) throw IllegalStateException("Listener is already registered")
        Bukkit.getPluginManager().registerEvents(this, Main.plugin)
        this.registered = true
    }

    /**
     * Unregister this listener
     * @author Kylepoops
     * @exception IllegalStateException if this listener is not registered or have been unregistered
     */
    fun unregister() {
        if (!registered) throw IllegalStateException("Listener is not registered")
        if (unregistered) throw IllegalStateException("Listener is already unregistered")
        HandlerList.unregisterAll(this)
        this.unregistered = true
    }

    /**
     * If the survivor died, finish the game with result of hunter win
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity == game.survivor) game.finish(GameResult.HUNTER_WIN)
    }

    /**
     * If the ender dragon is killed, finish the game with result of survivor win
     * @author Kylepoops
     */
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity.type == EntityType.ENDER_DRAGON && event.entity.world == game.worlds.theEnd) {
            game.finish(GameResult.SURVIVOR_WIN)
        }
    }

    /**
     * If the survivor quit, finish the game with result of survivor quit.
     * If hunter quit, remove it from the game.
     * If all hunters quit, finish the game with result of hunter quit.
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player == game.survivor) game.finish(GameResult.SURVIVOR_QUIT)
        if (event.player in game.hunters) game.removeHunter(event.player)
        if (game.hunters.isEmpty()) game.finish(GameResult.HUNTER_QUIT)
    }

    /**
     * Prevent players from moving during preparing state
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (game.started && game.startTime + 10000 > System.currentTimeMillis()) {
            event.isCancelled = true
        }
    }

    /**
     * Prevent players from interacting during preparing state
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (game.started && game.startTime + 10000 > System.currentTimeMillis()) {
            event.isCancelled = true
        }
    }
}