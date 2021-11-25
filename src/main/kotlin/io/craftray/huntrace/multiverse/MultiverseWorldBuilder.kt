package io.craftray.huntrace.multiverse

import io.craftray.huntrace.game_object.Spawnpoint
import org.bukkit.World
import org.bukkit.WorldType

@Suppress("LocalVariableName", "PrivatePropertyName")
class MultiverseWorldBuilder(private val name: String) {
    private val MVCore = MultiverseManager.MVCore
    var environment: World.Environment = World.Environment.NORMAL
    var type: WorldType = WorldType.NORMAL
    var spawnpoint: Spawnpoint = Spawnpoint.default()
    var generateStructures = true
    var seed: String? = null

    fun build(): World {
        MVCore.mvWorldManager.addWorld(name, environment, seed, type, generateStructures, null)
        val MVWorld = MVCore.mvWorldManager.getMVWorld(name)
        if (spawnpoint.isSet()) { MVWorld.spawnLocation = spawnpoint.get() }
        return MVWorld.cbWorld!!
    }

}