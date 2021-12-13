package io.craftray.huntrace.util.rule

import io.craftray.huntrace.util.objects.Distance
import io.craftray.huntrace.util.objects.Spawnpoint
import org.bukkit.WorldType
import java.io.Serializable
import kotlin.random.Random

sealed interface Rule

/**
 * The world rule
 * @param type The type of world
 * @param spawnpoint The spawnpoint of the world
 * @param seed The seed of the world
 * @param structures whether the world will generate structures
 */
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

/**
 * The compass rule
 * @param updateInterval The interval in ticks between compass updates
 * @param deception whether the compass will be deceived randomly
 * @param crossWorldTrack whether the compass will track across worlds
 * @param distanceLimit The distance limit of the compass
 */
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