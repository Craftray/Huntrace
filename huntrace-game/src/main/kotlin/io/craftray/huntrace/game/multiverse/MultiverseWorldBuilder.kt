package io.craftray.huntrace.game.multiverse

import io.craftray.huntrace.objects.Spawnpoint
import org.bukkit.World
import org.bukkit.WorldType

// wrapped the MultiverseCore#createWorld method to prevent null arguments
@Suppress("LocalVariableName", "PrivatePropertyName", "VariableNaming")
class MultiverseWorldBuilder(private val name: String) {
    private val MVCore = MultiverseManager.MVCore
    var environment: World.Environment = World.Environment.NORMAL
    var type: WorldType = WorldType.NORMAL
    var spawnpoint: Spawnpoint = Spawnpoint.default()
    var generateStructures = true
    var seed: String? = null

    /**
     * build a world by Multiverse
     * @author Kylepoops
     * @return the built world
     */
    fun build(): World {
        MVCore.mvWorldManager.addWorld(name, environment, seed, type, generateStructures, null)
        val MVWorld = MVCore.mvWorldManager.getMVWorld(name)
        if (spawnpoint.isSet()) { MVWorld.spawnLocation = spawnpoint.getByWorld(MVWorld.cbWorld) }
        return MVWorld.cbWorld
    }
}