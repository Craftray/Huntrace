package io.craftray.huntrace.game

import org.bukkit.entity.Player

fun Game.addHunter(player: Player) = this.players.addHunter(player)

fun Game.removeHunter(player: Player) = this.players.removeHunter(player)

var Game.survivor
        get() = this.players.getSurvivor()
        set(value) { this.players.setSurvivor(value) }

val Game.hunters
        get() = this.players.getHunters()
