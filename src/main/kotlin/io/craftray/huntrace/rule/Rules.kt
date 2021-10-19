package io.craftray.huntrace.rule

import io.craftray.huntrace.inlinehelper.Distance
import io.craftray.huntrace.inlinehelper.Seed
import io.craftray.huntrace.inlinehelper.Spawnpoint
import io.craftray.huntrace.inlinehelper.TPS
import org.bukkit.Location
import org.bukkit.WorldType

interface Rule

data class WorldRule(
    var type: WorldType = WorldType.NORMAL,
    var spawnpoint: Spawnpoint = Spawnpoint.default(),
    var seed: Seed = Seed.random(),
    var structures: Boolean = false
) : Rule

data class CompassRule(
    var updateInterval: TPS = TPS(20),
    var displayDistance: Boolean = false,
    var crossWorldTrack: Boolean = false,
    var distanceLimit: Distance = Distance.unlimited()
)