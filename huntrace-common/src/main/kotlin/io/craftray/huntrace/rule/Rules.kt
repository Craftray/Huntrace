package io.craftray.huntrace.rule

import io.craftray.huntrace.objects.Distance
import io.craftray.huntrace.objects.Spawnpoint
import org.bukkit.WorldType
import java.io.Serializable

interface Rule

data class WorldRule(
    var type: WorldType = WorldType.NORMAL,
    var spawnpoint: Spawnpoint = Spawnpoint.default(),
    var seed: String? = null,
    var structures: Boolean = true
) : Rule, Serializable {
    companion object {
        const val serialVersionUID = 6409623993095011L
    }
}

data class CompassRule(
    var updateInterval: Long = 20L,
    var displayDistance: Boolean = false,
    var deception: Boolean = false,
    var crossWorldTrack: Boolean = false,
    var distanceLimit: Distance = Distance.unlimited()
) : Rule, Serializable {
    companion object {
        const val serialVersionUID = 1886929089669138L
    }
}