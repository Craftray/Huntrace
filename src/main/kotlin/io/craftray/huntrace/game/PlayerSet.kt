package io.craftray.huntrace.game

import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.jvm.Throws

class PlayerSet {
    private val hunters = mutableSetOf<Player>()
    private lateinit var survivor: Player
    private var lock = false

    private val priviousLocations = mutableMapOf<Player, Location>()

    @Throws(IllegalStateException::class)
    fun lock() {
        if (lock) throw IllegalStateException("PlayerSet is already locked")
        lock = true
    }

    @Throws(IllegalStateException::class)
    fun addHunter(player: Player) {
        if (lock) throw IllegalStateException("Game has been started")
        this.hunters.add(player)
        this.priviousLocations[player] = player.location
    }

    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        if (lock && player.isOnline) throw IllegalStateException("Game has been started")
        this.hunters.remove(player)
        this.priviousLocations.remove(player)
    }

    @Throws(IllegalStateException::class)
    fun setSurvivor(player: Player) {
        if (lock) throw IllegalStateException("Game has been started")
        if (player != survivor) {
            if (this::survivor.isInitialized) {
                this.priviousLocations.remove(survivor)
            }
            this.survivor = player
            this.priviousLocations[player] = player.location
        }
    }

    fun getSurvivor() = survivor

    fun getHunters() = hunters

    fun getPreviousLocation(player: Player): Location {
        return priviousLocations[player] ?: throw IllegalStateException("Player ${player.name} is not in the game")
    }
}