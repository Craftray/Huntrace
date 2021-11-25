package io.craftray.huntrace.game

import org.bukkit.entity.Player

fun Game.addHunter(player: Player) = this.players.addHunter(player)

fun Game.removeHunter(player: Player) = this.players.removeHunter(player)

var Game.survivor
        get() = this.players.getSurvivor()
        set(value) { this.players.setSurvivor(value) }

val Game.hunters
        get() = this.players.getHunters()

fun Game.teleportFrom() {
        this.survivor.let { it.teleport(players.getPreviousLocation(it)) }
        this.hunters.forEach { it.teleport(players.getPreviousLocation(it)) }
}

fun Game.teleportTo() {
        this.survivor.teleport(worlds.overworld.spawnLocation)
        this.hunters.forEach { it.teleport(worlds.overworld.spawnLocation) }
}
