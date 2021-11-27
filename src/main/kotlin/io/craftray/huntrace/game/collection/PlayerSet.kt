package io.craftray.huntrace.game.collection

import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.jvm.Throws

class PlayerSet {
    val hunters = mutableSetOf<Player>()
    val survivors = mutableSetOf<Player>()
    val spectators = mutableSetOf<Player>()

    private var lock = false
    private val previousLocations = mutableMapOf<Player, Location>()

    /**
     * Lock the set to prevent changes during the game is running
     * @author Kylepoops
     * @exception IllegalStateException if the set is already locked
     */
    @Throws(IllegalStateException::class)
    fun lock() {
        if (lock) throw IllegalStateException("PlayerSet is already locked")
        lock = true
    }

    /**
     * Add a hunter to this set
     * @author Kylepoops
     * @exception IllegalStateException if the set is locked
     */
    @Throws(IllegalStateException::class)
    fun addHunter(player: Player) {
        if (lock) throw IllegalStateException("Game has been started")
        this.hunters.add(player)
    }

    /**
     * Add a survivor to this set
     * @author Kylepoops
     * @exception IllegalStateException if the set is locked
     */
    fun addSurvivor(player: Player) {
        if (lock) throw IllegalStateException("Game has been started")
        this.survivors.add(player)
    }

    /**
     * Removes a player from the set
     * @author Kylepoops
     * @exception IllegalStateException if the set is locked
     */
    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        if (lock && player.isOnline && hunters.size <= 1)
            throw IllegalStateException("Cannot remove hunter when they is the only online one left after locked")
        this.hunters.remove(player)
        this.previousLocations.remove(player)
    }

    /**
     * Removes a survivor from the set
     * @author Kylepoops
     * @exception IllegalStateException if the set is locked
     */
    @Throws(IllegalStateException::class)
    fun removeSurvivor(player: Player) {
        if (lock && player.isOnline && survivors.size <= 1)
            throw IllegalStateException("Cannot remove survivor when they is the only online one left after locked")
        this.survivors.remove(player)
        this.previousLocations.remove(player)
    }

    fun addSpectator(player: Player) {
        this.spectators.add(player)
    }

    fun removeSpectator(player: Player) {
        this.spectators.remove(player)
        this.previousLocations.remove(player)
    }

    /**
     * Store the location of every player in the set
     * @author Kylepoops
     * @exception IllegalStateException if the set isn't locked
     */
    @Throws(IllegalStateException::class)
    fun storeLocation() {
        if (!lock) throw IllegalArgumentException("PlayerSet must be locked before storing locations")
        this.hunters.forEach { this.previousLocations[it] = it.location }
        this.survivors.forEach { this.previousLocations[it] = it.location }
        this.spectators.forEach { this.previousLocations[it] = it.location }
    }


    /**
     * Get the previous location of a player
     * @author Kylepoops
     * @param player the player to get the location of
     * @return the location
     */
    fun getPreviousLocation(player: Player): Location {
        return previousLocations[player] ?: throw IllegalStateException("Player ${player.name} is not in the game")
    }

    /**
     * Whether the player are in this set
     * @author Kylepoops
     * @param player the player to check
     * @return the result
     */
    fun contains(player: Player): Boolean {
        return this.hunters.contains(player) || this.survivors.contains(player)
    }
}