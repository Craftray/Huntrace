package io.craftray.huntrace.world

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.multiverse.MultiverseWorldBuilder
import io.craftray.huntrace.multiverse.MultiverseWorldManager
import io.craftray.huntrace.multiverse.MultiverseWorldManager.delete
import org.bukkit.World
import kotlin.random.Random

fun Game.generateWorlds() : WorldSet {
    val rule = this.rules.worldRule
    val worlds = WorldSet()

    val overworld = MultiverseWorldBuilder("${this.gameID}").apply {
        environment = World.Environment.NORMAL
        type = rule.type
        generateStructures = rule.structures
        seed = rule.seed.toString()
        spawnpoint = rule.spawnpoint
    }.build()

    val nether = MultiverseWorldBuilder("${this.gameID}_nether").apply {
        environment = World.Environment.NETHER
        type = rule.type
        generateStructures = rule.structures
        seed = rule.seed
    }.build()

    val theEnd = MultiverseWorldBuilder("${this.gameID}_the_end").apply {
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

fun Game.deleteWorlds() = this.worlds.run {
    this.overworld.delete()
    this.nether.delete()
    this.theEnd.delete()
}

fun Game.linkWorlds() {
    this.worlds.run {
        MultiverseWorldManager.linkWorlds(this.overworld, this.nether, this.theEnd)
        MultiverseWorldManager.linkInventories(this.overworld, this.nether, this.theEnd)
    }
}

fun Game.unlinkWorlds() {
    this.worlds.run {
        MultiverseWorldManager.unlinkWorlds(this.overworld, this.nether, this.theEnd)
        MultiverseWorldManager.unlinkInventories(this.overworld, this.nether, this.theEnd)
    }
}
