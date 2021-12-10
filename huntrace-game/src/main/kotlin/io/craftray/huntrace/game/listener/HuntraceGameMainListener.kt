package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import io.craftray.huntrace.game.event.HuntraceGameInventoryClickEvent
import io.craftray.huntrace.game.event.HuntraceGameSelectTargetEvent
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.concurrent.thread

class HuntraceGameMainListener(private val game: Game) : HuntraceGameListener() {
    private val targets = game.hunterTargets

    /**
     * If the survivor died, finish the io.craftray.huntrace.game with result of hunter win
     * @author Kylepoops
     */
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.entity !in this.game.survivors) return
        if (this.game.survivors.size == 1) {
            this.game.finish(GameResult.HUNTER_WIN)
        } else {
            this.game.turnToSpectator(event.entity)
        }
    }

    /**
     * If the ender dragon is killed, finish the io.craftray.huntrace.game with result of survivor win
     * @author Kylepoops
     */
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity.type == EntityType.ENDER_DRAGON && event.entity.world == this.game.worlds.theEnd) {
            this.game.finish(GameResult.SURVIVOR_WIN)
        }
    }

    /**
     * If the survivor quit, finish the io.craftray.huntrace.game with result of survivor quit.
     * If hunter quit, remove it from the io.craftray.huntrace.game.
     * If all hunters quit, finish the io.craftray.huntrace.game with result of hunter quit.
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

    /**
     * If a hunter right click compass, select the target for him
     * @author Kylepoops
     */
    @EventHandler
    fun onCompassUse(event: PlayerInteractEvent) {
        if (event.player.world !in game.worlds || event.player !in game.hunters) {
            return
        }
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val target = targets.targetOf(event.player)
        thread(true) { HuntraceGameSelectTargetEvent(game, event.player, target).callEvent() }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked.world in game.worlds) {
            HuntraceGameInventoryClickEvent(game, event)
        }
    }
}