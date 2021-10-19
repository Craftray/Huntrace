package io.craftray.huntrace.inlinehelper

import org.bukkit.Location
import kotlin.random.Random

@JvmInline
value class TPS(val value: Long)

@JvmInline
value class Seed(val value: Long) {

    companion object {
        fun random() = Seed(Random.Default.nextLong())
    }
}

@JvmInline
value class Distance(val value: Long) {
    fun limited() = value != -1L

    companion object {
        fun unlimited() = Distance(-1L)
    }
}

@JvmInline
value class Spawnpoint(val value: Location) {
    fun isSetted() = value.world == null

    companion object {
        fun default() = Spawnpoint(Location(null, 0.0, 0.0, 0.0))
    }
}