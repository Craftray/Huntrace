package io.craftray.huntrace.game.collection

import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.jvm.Throws

class PlayerSet {
    val hunters = mutableSetOf<Player>()
    private lateinit var _survivor: Player
    private var lock = false
    var survivor: Player
        get() = _survivor
        set(value) {
            if (lock) throw IllegalStateException("Game has been started")
            if (value != this._survivor) {
                if (this::_survivor.isInitialized) {
                    this.previousLocations.remove(_survivor)
                }
                this._survivor = value
            }
        }

    private val previousLocations = mutableMapOf<Player, Location>()

    @Throws(IllegalStateException::class)
    fun lock() {
        if (lock) throw IllegalStateException("PlayerSet is already locked")
        lock = true
    }

    @Throws(IllegalStateException::class)
    fun addHunter(player: Player) {
        if (lock) throw IllegalStateException("Game has been started")
        this.hunters.add(player)
    }

    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        if (lock && player.isOnline) throw IllegalStateException("Game has been started")
        this.hunters.remove(player)
        this.previousLocations.remove(player)
    }

    fun storeLocation() {
        this.previousLocations[survivor] = this.survivor.location
        this.hunters.forEach { this.previousLocations[it] = it.location }
    }


    fun getPreviousLocation(player: Player): Location {
        return previousLocations[player] ?: throw IllegalStateException("Player ${player.name} is not in the game")
    }
}