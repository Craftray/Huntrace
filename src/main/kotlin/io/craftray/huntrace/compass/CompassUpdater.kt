package io.craftray.huntrace.compass

import io.craftray.huntrace.Main
import io.craftray.huntrace.Utils
import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.Game
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class CompassUpdater(val game: Game) {
    private val rule = game.rules.CompassRule
    private lateinit var trackRunnable: BukkitRunnable
    private lateinit var deceptionFindRunnable: BukkitRunnable
    private lateinit var deceptionRunnable: BukkitRunnable
    private val deceptionList = mutableListOf<Player>()
    private val hunters = game.players.hunters
    private val survivor = game.players.survivors
    private val worlds = game.worlds

    fun initTrack() {
        this.trackRunnable = bukkitRunnableOf {
            for (hunter in hunters.filterNot { it in deceptionList }) {
            for (item in hunter.inventory) {
                if (item.type == Material.COMPASS) {
                    val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                    compass.lodestone = survivor.location
                    item.itemMeta = compass
                }
            }}
        }

        this.trackRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)
    }

    fun initDeception() {
        this.deceptionFindRunnable = bukkitRunnableOf {
            for (hunter in hunters) {
                if (!Utils.randomBoolean(0.10F)) {
                    break
                }

                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val itemStack = ItemStack(Material.COMPASS)
                        itemStack.itemMeta.lore = listOf("§7§lDeception")
                        item.itemMeta = itemStack.itemMeta
                        deceptionList.add(hunter)
                        break
                    }
                }
            }
        }

        this.deceptionRunnable = bukkitRunnableOf {
            for (hunter in deceptionList) {
            for (item in hunter.inventory) {
                if (item.type == Material.COMPASS) {
                    val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                    compass.lodestone = survivor.location.multiply(Random.nextDouble(2.0))
                    item.itemMeta = compass
                }
            }}
        }

        this.deceptionFindRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)

        this.deceptionRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)
    }

    fun restoreCompass() {
        for (hunter in hunters) {
        for (item in hunter.inventory) {
            if (item.type == Material.COMPASS) {
                item.itemMeta = ItemStack(Material.COMPASS).itemMeta
            }
        }}
    }

    fun stopTrack() {
        this.trackRunnable.cancel()
    }

    fun stopDeception() {
        this.deceptionFindRunnable.cancel()
        this.deceptionRunnable.cancel()
    }
}