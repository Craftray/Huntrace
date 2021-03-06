package io.craftray.huntrace.interaction

import io.craftray.huntrace.game.event.HuntraceGameInventoryClickEvent
import io.craftray.huntrace.game.event.HuntraceGameSelectTargetEvent
import io.craftray.huntrace.util.owningPlayer
import io.craftray.huntrace.util.runnable.BukkitRunnableWrapper
import io.craftray.huntrace.util.toSkull
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import taboolib.common.platform.event.SubscribeEvent

/**
 * The class for handling target selecting of hunters.
 */
@Suppress("unused")
object InGameTargetSelector {

    @SubscribeEvent
    fun onSelect(event: HuntraceGameSelectTargetEvent) {
        val survivors = event.game.survivors
        val menu = buildMenu("Select Target") {
            this.survivors = survivors
        }
        // InventoryOpenEvent can only be triggered synchronously
        BukkitRunnableWrapper.submit { event.hunter.openInventory(menu) }
    }

    @SubscribeEvent
    fun onClick(event: HuntraceGameInventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.holder is Holder) {
            event.isCancelled = true
            val item = event.currentItem
            if (item.type == Material.PLAYER_HEAD) {
                item.owningPlayer?.let { event.game.setTarget(event.player, it) }
            }
        }
    }

    class Holder : InventoryHolder {
        override fun getInventory() = Bukkit.createInventory(this, 0, Component.empty())
    }

    class GUI(private val title: String) {
        lateinit var survivors: Collection<Player>

        @Suppress("SpreadOperator")
        fun build(): Inventory {
            val inv = Bukkit.createInventory(Holder(), 54, Component.text(title))
            return inv.apply { addItem(*survivors.map(Player::toSkull).toTypedArray()) }
        }
    }

    @Suppress("SameParameterValue")
    private inline fun buildMenu(title: String, init: GUI.() -> Unit) = GUI(title).apply(init).build()
}