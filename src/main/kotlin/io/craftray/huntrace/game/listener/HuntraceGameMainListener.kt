package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

class HuntraceGameMainListener(private val game: Game) : HuntraceGameListener() {

    /**
     * If the survivor died, finish the game with result of hunter win
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity !in this.game.survivors) return
        if (this.game.survivors.size == 1) this.game.finish(GameResult.HUNTER_WIN)
        else this.game.turnToSpectator(event.entity)
    }

    /**
     * If the ender dragon is killed, finish the game with result of survivor win
     * @author Kylepoops
     */
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity.type == EntityType.ENDER_DRAGON && event.entity.world == this.game.worlds.theEnd) {
            this.game.finish(GameResult.SURVIVOR_WIN)
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
        if (event.player in this.game.survivors) {
            if (this.game.survivors.size == 1) {
                this.game.finish(GameResult.SURVIVOR_QUIT)
                return
            }
            this.game.removeSurvivor(event.player)
        } else if (event.player in this.game.hunters) {
            if (this.game.hunters.size == 1) {
                this.game.finish(GameResult.HUNTER_QUIT)
                return
            }
            this.game.removeHunter(event.player)
        }
    }
}