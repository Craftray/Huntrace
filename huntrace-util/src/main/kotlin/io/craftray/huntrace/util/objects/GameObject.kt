@file:Suppress("DataClassPrivateConstructor")

package io.craftray.huntrace.util.objects

import org.bukkit.Location
import org.bukkit.World

/**
 * Wrapper to check the maximum tracking distance
 * @param value The maximum distance
 */
data class Distance private constructor(private val value: Long) {
    fun isLimited() = value > 0

    fun get(): Long {
        check(isLimited()) { "cannot invoke get() on this instance because distance is not limited" }
        return value
    }

    companion object {
        /**
         * set the distance to unlimited
         */
        fun unlimited() = Distance(-1L)

        /**
         * limit the maximum tracking distance to the given value
         */
        fun limited(value: Long): Distance {
            require(value > 0) { "distance must be greater than 0" }
            return Distance(value)
        }
    }
}

/**
 * location need to set world so the wrapper here is made
 * @param x x coordinate
 * @param y y coordinate
 * @param z z coordinate
 */
data class Spawnpoint private constructor(
    private val x: Double?,
    private val y: Double?,
    private val z: Double?
) {

    val isSet
        get() = x != null && z != null

    fun getByWorld(world: World): Location {
        check(isSet) { "cannot invoke get() on this instance because value is not set" }
        return y?.let { Location(world, x!!, y, z!!) }
            ?: Location(world, x!!, world.getHighestBlockYAt(x.toInt(), z!!.toInt()).toDouble(), z)
    }

    companion object {
        /**
         * Use the default spawnpoint
         */
        fun default() = Spawnpoint(null, null, null)

        /**
         * Set the spawnpoint with three coordinates
         * @param x x coordinate
         * @param y y coordinate, default is null which means using the highest y
         * @param z z coordinate
         */
        fun at(x: Double, y: Double? = null, z: Double) = Spawnpoint(x, y, z)
    }
}