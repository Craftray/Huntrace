package io.craftray.huntrace.game.schedular

import io.craftray.huntrace.Utils
import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.Utils.literalDistanceOf
import io.craftray.huntrace.Utils.transformWorld
import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.event.HuntraceGameCompassUpdateEvent
import io.craftray.huntrace.game.event.HuntraceGameCompassUpdateEvent.Result
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

@Suppress("PrivatePropertyName")
class CompassUpdater(val game: Game) {
    private val rule = game.rules.compassRule
    private val trackRunnableMap = mutableMapOf<Player, BukkitRunnable>()
    private lateinit var deceptionFindRunnable: BukkitRunnable
    private lateinit var deceptionRunnable: BukkitRunnable
    private val deceptionList = mutableListOf<Player>()
    private val targets = game.hunterTargets
    private val hunters = game.hunters
    private val worlds = game.worlds
    private var started = false

    /**
     * Start the updater
     * @author Kylepoops
     */
    fun start() {
        check(!started) { "CompassUpdater is already started" }
        this.initTrack()
        if (this.rule.deception) { this.initDeception() }
        this.started = true
    }

    /**
     * Stop the updater
     * @author Kylepoops
     */
    fun stop() {
        check(started) { "CompassUpdater is not started" }
        this.stopTrack()
        if (this.rule.deception) { this.stopDeception() }
    }

    /**
     * Stop tracking for the player
     * @author Kylepoops
     * @param player The player to stop tracking
     */
    fun stopTrackFor(player: Player) {
        val run = this.trackRunnableMap[player]
        checkNotNull(run) { "Player is not tracking" }
        run.cancel()
        this.trackRunnableMap.remove(player)
    }

    /**
     * Start tracking
     * @author Kylepoops
     */
    private fun initTrack() {
        for (hunter in this.hunters) {
            val run = bukkitRunnableOf {
                if (hunter in deceptionList || hunter.world !in worlds) {
                    return@bukkitRunnableOf
                }
                val target = targets.targetOf(hunter)
                if (!rule.crossWorldTrack && hunter.world != target.world) {
                    HuntraceGameCompassUpdateEvent(game, Result.MISS, hunter).callEvent()
                }
                if (hunter.world.environment != World.Environment.THE_END &&
                    target.world.environment == World.Environment.THE_END
                ) {
                    restoreCompass(hunter.inventory.itemInMainHand)
                    HuntraceGameCompassUpdateEvent(game, Result.MISS, hunter).callEvent()
                    return@bukkitRunnableOf
                }
                val distance = hunter.location.literalDistanceOf(target.location)
                if (!this.isDistanceValid(distance)) {
                    HuntraceGameCompassUpdateEvent(game, Result.MISS, hunter).callEvent()
                    return@bukkitRunnableOf
                }
                for (item in hunter.inventory) {
                    if (item.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        compass.lodestone = target.location.transformWorld(hunter.world)
                        item.itemMeta = compass
                        HuntraceGameCompassUpdateEvent(game, Result.SUCCESS, hunter).callEvent()
                    }
                }
            }
            this.trackRunnableMap[hunter] = run
        }

        trackRunnableMap.forEach {
            it.value.runTaskTimer(Game.plugin, 600, rule.updateInterval)
        }
    }

    /**
     * Start deceiving
     * @author Kylepoops
     */
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
                        this.deceptionList.add(hunter)
                        break
                    }
                }

                bukkitRunnableOf {
                    this.deceptionList.remove(hunter)
                }.runTaskLaterAsynchronously(Game.plugin, 300)
            }
        }

        this.deceptionRunnable = bukkitRunnableOf {
            for (hunter in deceptionList) {
            for (item in hunter.inventory) {
                if (item.type == Material.COMPASS) {
                    val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                    compass.lodestone = hunter.location.multiply(Random.nextDouble(2.0))
                    item.itemMeta = compass
                    HuntraceGameCompassUpdateEvent(game, Result.DECEPTION, hunter).callEvent()
                }
            }}
        }

        this.deceptionFindRunnable.runTaskTimerAsynchronously(Game.plugin, 600, rule.updateInterval)

        this.deceptionRunnable.runTaskTimer(Game.plugin, 600, rule.updateInterval)
    }

    @Throws(IllegalArgumentException::class)
    private fun restoreCompass(item: ItemStack) {
        if (item.type == Material.COMPASS) {
            item.itemMeta = ItemStack(Material.COMPASS).itemMeta
        } else {
            throw IllegalArgumentException("This item is not a compass")
        }
    }

    private fun stopTrack() = this.trackRunnableMap.forEach {
        it.value.cancel()
    }

    private fun stopDeception() {
        this.deceptionFindRunnable.cancel()
        this.deceptionRunnable.cancel()
    }

    private fun isDistanceValid(distance: Double) = !(rule.distanceLimit.isLimited() && distance >= rule.distanceLimit.get())
}