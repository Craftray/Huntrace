package io.craftray.huntrace.game.compass

import io.craftray.huntrace.Main
import io.craftray.huntrace.Utils
import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.hunters
import io.craftray.huntrace.game.survivor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.jvm.Throws
import kotlin.random.Random

class CompassUpdater(game: Game) {
    private val rule = game.rules.CompassRule
    private lateinit var trackRunnable: BukkitRunnable
    private lateinit var deceptionFindRunnable: BukkitRunnable
    private lateinit var deceptionRunnable: BukkitRunnable
    private val deceptionList = mutableListOf<Player>()
    private val hunters = game.hunters
    private val survivor = game.survivor
    private val worlds = game.worlds

    fun init() {
        this.initTrack()
        if (this.rule.deception) { this.initDeception() }
    }

    fun stop() {
        this.stopTrack()
        if (this.rule.deception) { this.stopDeception() }
    }

    private fun initTrack() {
        this.trackRunnable = bukkitRunnableOf {
            val activeHunters = hunters.asSequence().filter { it !in deceptionList}
            for (hunter in activeHunters.filter {it.world == survivor.world }) {
                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        compass.lodestone = survivor.location
                        item.itemMeta = compass
                        this.trackMassage(hunter, survivor)
                    }
                }
            }
            if (survivor.world == worlds.overworld) {
                for (hunter in activeHunters.filter {it.world == worlds.nether }) {
                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        compass.lodestone = survivor.location.multiply(8.0)
                        item.itemMeta = compass
                        this.trackMassage(hunter, survivor)
                    }
                }}
            }
            if (survivor.world == worlds.nether) {
                for (hunter in activeHunters.filter {it.world == worlds.overworld }) {
                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        compass.lodestone = survivor.location.multiply(0.125)
                        item.itemMeta = compass
                        this.trackMassage(hunter, survivor)
                    }
                }}
            }
            if (survivor.world == worlds.theEnd) {
                for (hunter in activeHunters.filterNot {it.world == worlds.theEnd }) {
                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        restoreCompass(item)
                    }
                }}
            }
        }

        this.trackRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)
    }

    private fun initDeception() {
        this.deceptionFindRunnable = bukkitRunnableOf {
            for (hunter in hunters) {
                if (!Utils.randomBoolean(0.10F)) {
                    break
                }

                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val itemStack = ItemStack(Material.COMPASS)
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
                    this.deceptionMessage(hunter, survivor)
                }
            }}
        }

        this.deceptionFindRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)

        this.deceptionRunnable.runTaskTimer(Main.plugin, 600, rule.updateInterval)
    }

    @Throws(IllegalArgumentException::class)
    private fun restoreCompass(item: ItemStack) {
        if (item.type == Material.COMPASS) {
            item.itemMeta = ItemStack(Material.COMPASS).itemMeta
        } else {
            throw IllegalArgumentException("This item is not a compass")
        }
    }

    private fun trackMassage(hunter: Player, survivor: Player) {
        hunter.sendActionBar(Component.text("You are now tracking ").color(NamedTextColor.GRAY)
                                    .append(Component.text(survivor.name).color(NamedTextColor.GREEN))
                                    .append(Component.text(".").color(NamedTextColor.GRAY)))
    }

    private fun deceptionMessage(hunter: Player, survivor: Player) {
        hunter.sendActionBar(Component.text("Signal is not good.").color(NamedTextColor.RED))
    }

    private fun stopTrack() {
        this.trackRunnable.cancel()
    }

    private fun stopDeception() {
        this.deceptionFindRunnable.cancel()
        this.deceptionRunnable.cancel()
    }
}