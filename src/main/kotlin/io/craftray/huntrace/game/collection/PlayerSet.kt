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
     * Removes a player from the set
     * @author Kylepoops
     * @exception IllegalStateException if the set is locked
     */
    @Throws(IllegalStateException::class)
    fun removeHunter(player: Player) {
        if (lock && player.isOnline) throw IllegalStateException("Game has been started")
        this.hunters.remove(player)
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
        this.previousLocations[survivor] = this.survivor.location
        this.hunters.forEach { this.previousLocations[it] = it.location }
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
        return this.hunters.contains(player) || this.survivor == player
    }
}