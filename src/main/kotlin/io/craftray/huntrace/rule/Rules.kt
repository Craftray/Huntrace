package io.craftray.huntrace.rule

import io.craftray.huntrace.game_object.Distance
import io.craftray.huntrace.game_object.Spawnpoint
import org.bukkit.WorldType
import kotlin.random.Random

interface Rule

data class WorldRule(
    var type: WorldType = WorldType.NORMAL,
    var spawnpoint: Spawnpoint = Spawnpoint.default(),
    var seed: Long = Random.Default.nextLong(),
    var structures: Boolean = true
) : Rule

data class CompassRule(
    var updateInterval: Long = 20L,
    var displayDistance: Boolean = false,
    var crossWorldTrack: Boolean = false,
    var distanceLimit: Distance = Distance.unlimited()
) : Rule