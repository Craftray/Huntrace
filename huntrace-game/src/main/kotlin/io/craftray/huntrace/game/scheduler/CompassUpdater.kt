package io.craftray.huntrace.game.scheduler

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.event.HuntraceGameCompassUpdateEvent
import io.craftray.huntrace.game.event.HuntraceGameCompassUpdateEvent.Result
import io.craftray.huntrace.util.BasicUtils
import io.craftray.huntrace.util.BasicUtils.literal2DDistanceOf
import io.craftray.huntrace.util.BasicUtils.transformWorld
import io.craftray.huntrace.util.runnable.BukkitRunnableWrapper
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random

@Suppress("PrivatePropertyName")
class CompassUpdater(val game: Game) {
    private val rule = game.rules.compassRule
    private val trackTaskMap = mutableMapOf<Player, BukkitTask>()
    private lateinit var deceptionFindRunnable: BukkitTask
    private lateinit var deceptionRunnable: BukkitTask
    private val deceptionList = mutableListOf<Player>()
    private val targets = game.hunterTargets
    private val hunters = game.hunters
    private val worlds = game.worlds
    private var started = false

    /**
     * Start the updater
     * @author Kylepoops
     */
    fun init() {
        check(!started) { "CompassUpdater is already started" }
        this.initHunters()
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
        val run = this.trackTaskMap[player]
        checkNotNull(run) { "Player is not tracking" }
        run.cancel()
        this.trackTaskMap.remove(player)
    }

    /**
     * Start tracking
     * @author Kylepoops
     */
    private fun initTrack() {
        // create a runnable for every hunter
        for (hunter in this.hunters) {
            val run = BukkitRunnableWrapper.submitTimer(600L, rule.updateInterval) {
                if (hunter in deceptionList || hunter.world !in worlds) {
                    return@submitTimer
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
                    return@submitTimer
                }
                val distance = hunter.location.literal2DDistanceOf(target.location)
                if (!this.isDistanceValid(distance)) {
                    HuntraceGameCompassUpdateEvent(game, Result.MISS, hunter).callEvent()
                    return@submitTimer
                }
                for (item in hunter.inventory) {
                    if (item?.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        item.itemMeta = compass.apply {
                            isLodestoneTracked = false
                            lodestone = target.location.transformWorld(hunter.world)
                            displayName(Component.text("Tracker of ${target.name}"))
                        }
                        HuntraceGameCompassUpdateEvent(game, Result.SUCCESS, hunter).callEvent()
                    }
                }
            }

            this.trackTaskMap[hunter] = run
        }
    }

    /**
     * Start deceiving
     * @author Kylepoops
     */
    private fun initDeception() {
        this.deceptionFindRunnable = BukkitRunnableWrapper.submitTimerAsync(600L, rule.updateInterval) {
            for (hunter in hunters) {
                if (!BasicUtils.randomBoolean(0.10F)) {
                    break
                }

                for (item in hunter.inventory) {
                    if (item?.type == Material.COMPASS) {
                        val itemStack = ItemStack(Material.COMPASS)
                        item.itemMeta = itemStack.itemMeta
                        this.deceptionList.add(hunter)
                        break
                    }
                }

                BukkitRunnableWrapper.submitDelayedAsync(300L) {
                    this.deceptionList.remove(hunter)
                }
            }
        }

        this.deceptionRunnable = BukkitRunnableWrapper.submitTimerAsync(600, rule.updateInterval) {
            for (hunter in deceptionList) {
                for (item in hunter.inventory) {
                    if (item?.type == Material.COMPASS) {
                        val compass = item.itemMeta as org.bukkit.inventory.meta.CompassMeta
                        compass.lodestone = hunter.location.multiply(Random.nextDouble(2.0))
                        item.itemMeta = compass
                        HuntraceGameCompassUpdateEvent(game, Result.DECEPTION, hunter).callEvent()
                    }
                }
            }
        }
    }

    // literally give each of them a compass
    private fun initHunter(hunter: Player) = hunter.inventory.addItem(ItemStack(Material.COMPASS))

    private fun initHunters() = this.hunters.forEach(::initHunter)

    @Throws(IllegalArgumentException::class)
    private fun restoreCompass(item: ItemStack) {
        if (item.type == Material.COMPASS) {
            item.itemMeta = ItemStack(Material.COMPASS).itemMeta
        } else {
            throw IllegalArgumentException("This item is not a compass")
        }
    }

    private fun stopTrack() = this.trackTaskMap.forEach {
        it.value.cancel()
    }

    private fun stopDeception() {
        this.deceptionFindRunnable.cancel()
        this.deceptionRunnable.cancel()
    }

    private fun isDistanceValid(distance: Double) = !(rule.distanceLimit.isLimited() && distance >= rule.distanceLimit.get())
}