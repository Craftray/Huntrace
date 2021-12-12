package io.craftray.huntrace.interaction

import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.event.HuntraceGameInventoryClickEvent
import io.craftray.huntrace.game.event.HuntraceGameSelectTargetEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.common.platform.event.SubscribeEvent

@Suppress("unused")
object InGameTargetSelector {
    @SubscribeEvent
    fun onSelect(event: HuntraceGameSelectTargetEvent) {
        val survivors = event.game.survivors
        val menu = buildMenu("Select Target") {
            this.survivors = survivors
        }
        // InventoryOpenEvent can only be triggered synchronously
        bukkitRunnableOf { event.hunter.openInventory(menu) }.runTask(InteractionBase.plugin)
    }

    @SubscribeEvent
    fun onClick(event: HuntraceGameInventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.holder is Holder) {
            event.isCancelled = true
            val player = event.player
            val item = event.currentItem
            if (item.type == Material.PLAYER_HEAD) {
                val meta = item.itemMeta as? SkullMeta ?: return
                meta.owningPlayer?.name?.let { Bukkit.getPlayer(it) }?.let {
                    event.game.setTarget(player, it)
                }
            }
        }
    }

    class Holder : InventoryHolder {
        override fun getInventory() = Bukkit.createInventory(this, 0, Component.empty())
    }

    class GUI(val title: String) {
        lateinit var survivors: Collection<Player>

        fun build(): Inventory {
            return Bukkit.createInventory(Holder(), 54, Component.text(title)).also { inv ->
                survivors.map { player ->
                    return@map ItemStack(Material.PLAYER_HEAD).apply {
                        itemMeta = itemMeta.also { meta ->
                            meta as SkullMeta
                            meta.owningPlayer = player
                        }
                    }
                }.forEach {
                    inv.addItem(it)
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun buildMenu(title: String, init: GUI.() -> Unit) = GUI(title).apply(init).build()
}