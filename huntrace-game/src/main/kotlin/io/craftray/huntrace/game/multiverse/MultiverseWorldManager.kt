package io.craftray.huntrace.game.multiverse

import com.onarandombox.multiverseinventories.share.Sharables
import io.craftray.huntrace.util.io.deepDeleteAsync
import org.bukkit.PortalType
import org.bukkit.World

object MultiverseWorldManager {
    /**
     * Case to multiverse world
     * @author Kylepoops
     */
    fun World.toMVWorld() = MultiverseManager.MVCore.mvWorldManager.getMVWorld(this)!!

    /**
     * Delete the world
     * @author Kylepoops
     */
    fun World.delete() = worldFolder.deepDeleteAsync()

    /**
     * Link the three dimensions' gateway
     * @param overworld the overworld
     * @param nether the nether
     * @param theEnd the end
     */
    fun linkWorlds(overworld: World, nether: World, theEnd: World) {
        // respawn location
        overworld.toMVWorld().setRespawnToWorld(overworld.name)
        nether.toMVWorld().setRespawnToWorld(overworld.name)
        theEnd.toMVWorld().setRespawnToWorld(overworld.name)

        // link each of them
        MultiverseManager.MVNP.run {
            this.addWorldLink(overworld.name, nether.name, PortalType.NETHER)
            this.addWorldLink(nether.name, overworld.name, PortalType.NETHER)

            this.addWorldLink(overworld.name, theEnd.name, PortalType.ENDER)
            this.addWorldLink(theEnd.name, overworld.name, PortalType.ENDER)
        }
    }

    /**
     * Link players' inventory of the three dimensions
     * @param overworld the overworld
     * @param nether the nether
     * @param theEnd the end
     */
    fun linkInventories(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVInv.run {
            val group = this.groupManager.newEmptyGroup(overworld.name).apply {
                this.addWorld(overworld)
                this.addWorld(nether)
                this.addWorld(theEnd)
            }

            group.shares.addAll(Sharables.allOf())

            this.groupManager.updateGroup(group)
        }
    }

    /**
     * Unlink the three dimensions' gateway
     * @author Kylepoops
     * @param overworld the overworld
     * @param nether the nether
     * @param theEnd the end
     */
    fun unlinkWorlds(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVNP.run {
            this.removeWorldLink(overworld.name, nether.name, PortalType.NETHER)
            this.removeWorldLink(nether.name, overworld.name, PortalType.NETHER)

            this.removeWorldLink(overworld.name, theEnd.name, PortalType.ENDER)
            this.removeWorldLink(theEnd.name, overworld.name, PortalType.ENDER)
        }
    }

    /**
     * Unlink players' inventory of the three dimensions
     * @author Kylepoops
     * @param overworld the overworld
     * @param nether the nether
     * @param theEnd the end
     */
    // should I leave the args here..?
    @Suppress("UNUSED_PARAMETER")
    fun unlinkInventories(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVInv.run {
            this.groupManager.getGroup(overworld.name)?.let {
                this.groupManager.removeGroup(it)
            }
        }
    }
}