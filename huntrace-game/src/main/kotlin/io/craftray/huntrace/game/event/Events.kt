@file:Suppress("unused")

package io.craftray.huntrace.game.event

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class HuntraceGameEvent(val game: Game, async: Boolean) : Event(async) {
    constructor(game: Game) : this(game, false)
}

/**
 * Thrown after a game finishes
 * @author Kylepoops
 */
class HuntraceGameFinishEvent(game: Game, val result: GameResult) : HuntraceGameEvent(game, true) {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a game starts
 * @author Kylepoops
 */
class HuntraceGameStartEvent(game: Game) : HuntraceGameEvent(game, true) {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a player joins a game
 * @author Kylepoops
 */
class HuntraceGameHunterQuitEvent(game: Game, val player: Player) : HuntraceGameEvent(game, true) {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a compass is updated in the io.craftray.huntrace.game
 * @author Kylepoops
 */
class HuntraceGameCompassUpdateEvent(
    game: Game,
    val result: Result,
    val hunter: Player,
    val target: Player = game.hunterTargets.targetOf(hunter)
) : HuntraceGameEvent(game, true) {
    override fun getHandlers() = handlerList

    enum class Result {
        SUCCESS,
        MISS,
        DECEPTION
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a player means to select a target
 * @author Kylepoops
 */
class HuntraceGameSelectTargetEvent(
    game: Game,
    val hunter: Player,
    val target: Player
) : HuntraceGameEvent(game, true) {
    override fun getHandlers() = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

class HuntraceGameInventoryClickEvent(
    game: Game,
    private val bukkitEvent: InventoryClickEvent
) : HuntraceGameEvent(game), Cancellable {
    val player: Player
        get() {
            return bukkitEvent.whoClicked as? Player
                ?: Bukkit.getPlayerExact(bukkitEvent.whoClicked.name)
                ?: error("Player is not exist") // which shouldn't happen'
        }

    val currentItem: ItemStack
        get() = bukkitEvent.currentItem ?: ItemStack(Material.AIR)

    val isRightClick: Boolean
        get() = bukkitEvent.isRightClick

    val isLeftClick: Boolean
        get() = bukkitEvent.isLeftClick

    val action: InventoryAction
        get() = bukkitEvent.action

    val inventory: Inventory
        get() = bukkitEvent.inventory

    override fun getHandlers() = handlerList

    override fun isCancelled() = bukkitEvent.isCancelled

    override fun setCancelled(cancel: Boolean) { bukkitEvent.isCancelled = cancel }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}