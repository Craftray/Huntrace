package io.craftray.huntrace.multiverse

import com.onarandombox.multiverseinventories.share.Sharables
import org.bukkit.PortalType
import org.bukkit.World

object MultiverseWorldManager {
    fun World.toMVWorld() = MultiverseManager.MVCore.mvWorldManager.getMVWorld(this)!!

    fun World.delete() = MultiverseManager.MVCore.mvWorldManager.deleteWorld(this.name)

    fun linkWorlds(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVNP.run {
            this.addWorldLink(overworld.name, nether.name, PortalType.NETHER)
            this.addWorldLink(nether.name, overworld.name, PortalType.NETHER)

            this.addWorldLink(overworld.name, theEnd.name, PortalType.ENDER)
            this.addWorldLink(theEnd.name, overworld.name, PortalType.ENDER)
        }
    }

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

    fun unlinkWorlds(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVNP.run {
            this.removeWorldLink(overworld.name, nether.name, PortalType.NETHER)
            this.removeWorldLink(nether.name, overworld.name, PortalType.NETHER)

            this.removeWorldLink(overworld.name, theEnd.name, PortalType.ENDER)
            this.removeWorldLink(theEnd.name, overworld.name, PortalType.ENDER)
        }
    }

    fun unlinkInventories(overworld: World, nether: World, theEnd: World) {
        MultiverseManager.MVInv.run {
            this.groupManager.getGroup(overworld.name)?.let {
                this.groupManager.removeGroup(it)
            }
        }
    }
}