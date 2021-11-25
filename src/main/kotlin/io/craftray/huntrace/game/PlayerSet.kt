package io.craftray.huntrace.game

import org.bukkit.entity.Player

class PlayerSet {
    val hunters = mutableSetOf<Player>()

    lateinit var survivors: Player
}