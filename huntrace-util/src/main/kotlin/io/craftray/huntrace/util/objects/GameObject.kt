@file:Suppress("DataClassPrivateConstructor")

package io.craftray.huntrace.util.objects

import org.bukkit.Location
import org.bukkit.World

data class Distance private constructor(private val value: Long) {
    fun isLimited() = value > 0

    fun get(): Long {
        check(isLimited()) { "cannot invoke get() on this instance because distance is not limited" }
        return value
    }

    companion object {
        fun unlimited() = Distance(-1L)

        fun limited(value: Long): Distance {
            require(value > 0) { "distance must be greater than 0" }
            return Distance(value)
        }
    }
}

// location need to set world so the wrapper here is made
data class Spawnpoint private constructor(
    private val x: Double?,
    private val y: Double?,
    private val z: Double?
) {
    fun isSet() = x != null && z != null

    fun getByWorld(world: World): Location {
        check(isSet()) { "cannot invoke get() on this instance because value is not set" }
        return y?.let { Location(world, x!!, y, z!!) }
            ?: Location(world, x!!, world.getHighestBlockYAt(x.toInt(), z!!.toInt()).toDouble(), z)
    }

    companion object {
        fun default() = Spawnpoint(null, null, null)

        fun at(x: Double, y: Double?, z: Double) = Spawnpoint(x, y, z)

        fun at(x: Double, z: Double) = Spawnpoint(x, null, z)
    }
}