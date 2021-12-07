package io.craftray.huntrace

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.sqrt

object Utils {
    const val OVERWORLD_TO_NETHER_MULTIPLIER = 0.125
    const val NETHER_TO_OVERWORLD_MULTIPLIER = 8.0

    inline fun bukkitRunnableOf(crossinline block: () -> Unit) = object : BukkitRunnable() {
        override fun run() {
            block()
        }
    }

    // return a random boolean with specific chance
    fun randomBoolean(chance: Float) = Math.random() < chance

    fun Location.transformWorld(target: World): Location {
        val env = this.world.environment
        val result = this.clone()
        if (env == World.Environment.NORMAL && target.environment == World.Environment.NETHER) {
            result.x *= NETHER_TO_OVERWORLD_MULTIPLIER
            result.z *= NETHER_TO_OVERWORLD_MULTIPLIER
        } else if (env == World.Environment.NETHER && target.environment == World.Environment.NORMAL) {
            result.x *= OVERWORLD_TO_NETHER_MULTIPLIER
            result.z *= OVERWORLD_TO_NETHER_MULTIPLIER
        } else if ((env == World.Environment.NORMAL || env == World.Environment.NETHER) && target.environment == World.Environment.THE_END) {
            throw IllegalArgumentException("Cannot transform rnv THE_END and other dimensions")
        }

        return result.apply { world = target }
    }

    fun Location.literalDistanceOf(other: Location): Double {
        val otherLoc = other.clone().transformWorld(this.world)
        val x = this.x - otherLoc.x
        val z = this.z - otherLoc.z
        return sqrt(x * x + z * z)
    }
}