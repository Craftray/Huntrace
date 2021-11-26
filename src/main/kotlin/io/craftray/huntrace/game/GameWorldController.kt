package io.craftray.huntrace.game

import io.craftray.huntrace.game.collection.WorldSet
import io.craftray.huntrace.multiverse.MultiverseWorldBuilder
import io.craftray.huntrace.multiverse.MultiverseWorldManager
import io.craftray.huntrace.multiverse.MultiverseWorldManager.delete
import org.bukkit.World

class GameWorldController(private val game: Game) {

    fun generateWorlds(): WorldSet {
        val rule = game.rules.worldRule
        val worlds = WorldSet()

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

        return worlds.apply {
            this.overworld = overworld
            this.nether = nether
            this.theEnd = theEnd
        }
    }

    fun deleteWorlds() = game.worlds.run {
        this.overworld.delete()
        this.nether.delete()
        this.theEnd.delete()
    }

    fun linkWorlds() {
        game.worlds.run {
            MultiverseWorldManager.linkWorlds(this.overworld, this.nether, this.theEnd)
            MultiverseWorldManager.linkInventories(this.overworld, this.nether, this.theEnd)
        }
    }

    fun unlinkWorlds() {
        game.worlds.run {
            MultiverseWorldManager.unlinkWorlds(this.overworld, this.nether, this.theEnd)
            MultiverseWorldManager.unlinkInventories(this.overworld, this.nether, this.theEnd)
        }
    }
}
