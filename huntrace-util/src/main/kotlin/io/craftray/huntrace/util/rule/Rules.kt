package io.craftray.huntrace.util.rule

import io.craftray.huntrace.util.objects.Distance
import io.craftray.huntrace.util.objects.Spawnpoint
import org.bukkit.WorldType
import java.io.Serializable
import kotlin.random.Random

sealed interface Rule

data class WorldRule(
    var type: WorldType = WorldType.NORMAL,
    var spawnpoint: Spawnpoint = Spawnpoint.default(),
    // seems like Multiverse will generate exactly the same world if we didn't specify the seed
    var seed: String? = Random.Default.nextLong().toString(),
    var structures: Boolean = true
) : Rule, Serializable {
    companion object {
        const val serialVersionUID = 6409623993095011L
    }
}

data class CompassRule(
    var updateInterval: Long = 60L,
    // var displayDistance: Boolean = false,
    var deception: Boolean = false,
    var crossWorldTrack: Boolean = false,
    var distanceLimit: Distance = Distance.unlimited()
) : Rule, Serializable {
    companion object {
        const val serialVersionUID = 1886929089669138L
    }
}