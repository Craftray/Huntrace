@file:Suppress("DataClassPrivateConstructor")

package io.craftray.huntrace.game_object

import org.bukkit.Location
import org.bukkit.World

data class Distance private constructor(private val value: Long) {
    fun isLimited() = value != -1L

    fun get() =
        if (isLimited()) value
        else throw IllegalStateException("cannot invoke get() on this instance because distance is not limited")

    companion object {
        fun unlimited() = Distance(-1L)

        fun limited(value: Long) = Distance(value)
    }
}

data class Spawnpoint private constructor(private val value: Location?) {
    fun isSet() = value == null

    fun get() =
        if (isSet()) value!!
        else throw IllegalStateException("cannot invoke get() on this instance because value is not set")

    class SpawnpointBuilder(private val world: World) {
        fun at(x: Int, y: Int, z: Int) = Spawnpoint(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
    }

    companion object {
        fun default() = Spawnpoint(null)

        fun of(world: World) = SpawnpointBuilder(world)
    }
}