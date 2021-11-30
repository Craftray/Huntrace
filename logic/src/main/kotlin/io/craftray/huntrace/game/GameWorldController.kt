package io.craftray.huntrace.game

import io.craftray.huntrace.game.collection.WorldCollection
import io.craftray.huntrace.game.multiverse.MultiverseWorldBuilder
import io.craftray.huntrace.game.multiverse.MultiverseWorldManager
import io.craftray.huntrace.game.multiverse.MultiverseWorldManager.delete
import org.bukkit.World

class GameWorldController(private val game: Game) {

    private var generated = false
    private var linked = false
    private var unlink = false
    private var deleted = false

    /**
     * Generate the three dimensions of a io.craftray.huntrace.game
     * @author Kylepoops
     * @return the world set
     * @exception IllegalStateException if the io.craftray.huntrace.game has already been generated
     */
    @Throws(IllegalStateException::class)
    fun generateWorlds(): WorldCollection {
        if (generated)  throw IllegalStateException("Worlds have already been generated")

        val rule = game.rules.worldRule
        val worlds = WorldCollection()

        val overworld = MultiverseWorldBuilder("${game.gameID}").apply {
            environment = World.Environment.NORMAL
            type = rule.type
            generateStructures = rule.structures
            seed = rule.seed.toString()
            spawnpoint = rule.spawnpoint
        }.build()

        val nether = MultiverseWorldBuilder("${game.gameID}_nether").apply {
            environment = World.Environment.NETHER
            type = rule.type
            generateStructures = rule.structures
            seed = rule.seed
        }.build()

        val theEnd = MultiverseWorldBuilder("${game.gameID}_the_end").apply {
            environment = World.Environment.THE_END
            type = rule.type
            generateStructures = rule.structures
            seed = rule.seed
        }.build()

        this.generated = true

        return worlds.apply {
            this.overworld = overworld
            this.nether = nether
            this.theEnd = theEnd
        }
    }

    /**
     * Delete the worlds of the io.craftray.huntrace.game
     * @author Kylepoops
     * @exception IllegalStateException if the worlds have not been generated or have been deleted
     */
    @Throws(IllegalStateException::class)
    fun deleteWorlds() = game.worlds.run {
        if (!generated) throw IllegalStateException("Worlds have not been generated")
        if (deleted) throw IllegalStateException("Worlds have already been deleted")
        this.overworld.delete()
        this.nether.delete()
        this.theEnd.delete()
        deleted = true
    }

    /**
     * Link the three dimensions
     * @author Kylepoops
     * @exception IllegalStateException if the worlds have not been generated or have been deleted or linked
     */
    @Throws(IllegalStateException::class)
    fun linkWorlds() = game.worlds.run {
        if (!generated) throw IllegalStateException("Worlds have not been generated")
        if (deleted) throw IllegalStateException("Worlds have already been deleted")
        if (linked) throw IllegalStateException("Worlds have already been linked")
        MultiverseWorldManager.linkWorlds(this.overworld, this.nether, this.theEnd)
        MultiverseWorldManager.linkInventories(this.overworld, this.nether, this.theEnd)
        linked = true
    }

    /**
     * Unlink the three dimensions
     * @author Kylepoops
     * @exception IllegalStateException if the worlds have not been generated or have been deleted, linked or unlinked
     */
    @Throws(IllegalStateException::class)
    fun unlinkWorlds() = game.worlds.run {
        if(!generated) throw IllegalStateException("Worlds have not been generated")
        if(deleted) throw IllegalStateException("Worlds have already been deleted")
        if (!linked) throw IllegalStateException("Worlds have not been linked")
        if (unlink) throw IllegalStateException("Worlds have already been unlinked")
        MultiverseWorldManager.unlinkWorlds(this.overworld, this.nether, this.theEnd)
        MultiverseWorldManager.unlinkInventories(this.overworld, this.nether, this.theEnd)
        unlink = false
    }
}
