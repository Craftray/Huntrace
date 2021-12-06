@file:Suppress("DataClassPrivateConstructor")

package io.craftray.huntrace.objects

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

data class Spawnpoint private constructor(private val value: Location?) {
    fun isSet() = value != null

    fun get(): Location {
        check(isSet()) { "cannot invoke get() on this instance because value is not set" }
        return value!!
    }

    class SpawnpointBuilder(private val world: World) {
        fun at(x: Int, y: Int, z: Int) = Spawnpoint(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
    }

    companion object {
        fun default() = Spawnpoint(null)

        fun of(world: World) = SpawnpointBuilder(world)
    }
}