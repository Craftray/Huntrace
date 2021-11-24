package io.craftray.huntrace.world

import io.craftray.huntrace.Game
import org.bukkit.World
import org.bukkit.WorldCreator

object WorldController {
    fun Game.generateWorlds() : WorldSet {
        val rule = this.rules.worldRule
        val worlds = WorldSet()

        val overworld = WorldCreator("${this.gameID}-overworld")
            .environment(World.Environment.NORMAL)
            .type(rule.type)
            .generateStructures(rule.structures)
            .seed(rule.seed)
            .createWorld()!!

        if (rule.spawnpoint.isSet()) overworld.spawnLocation = rule.spawnpoint.get()

        val nether = WorldCreator("${this.gameID}-nether")
            .environment(World.Environment.NETHER)
            .type(rule.type)
            .generateStructures(rule.structures)
            .seed(rule.seed)
            .createWorld()!!

        val theEnd = WorldCreator("${this.gameID}-the-end")
            .environment(World.Environment.THE_END)
            .type(rule.type)
            .generateStructures(rule.structures)
            .seed(rule.seed)
            .createWorld()!!

        return worlds.apply {
            this.overworld = overworld
            this.nether = nether
            this.theEnd = theEnd
        }
    }

    fun World.delete() {}

    fun Game.deleteWorlds() = this.worlds.run {
        this.overworld.delete()
        this.nether.delete()
        this.theEnd.delete()
    }
}