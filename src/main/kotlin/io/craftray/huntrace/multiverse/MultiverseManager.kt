package io.craftray.huntrace.multiverse

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals
import com.onarandombox.multiverseinventories.MultiverseInventories
import org.bukkit.Bukkit
import org.bukkit.World

object MultiverseManager {
    lateinit var MVCore: MultiverseCore
    lateinit var MVInv: MultiverseInventories
    lateinit var MVNP: MultiverseNetherPortals

    @Throws(IllegalArgumentException::class)
    fun initMultiverse() {
        val core = Bukkit.getServer().pluginManager.getPlugin("Multiverse-Core") as? MultiverseCore?
        val inv = Bukkit.getServer().pluginManager.getPlugin("Multiverse-Inventories") as? MultiverseInventories?
        val np = Bukkit.getServer().pluginManager.getPlugin("Multiverse-NetherPortals") as? MultiverseNetherPortals?

        if (core != null && inv != null && np != null) {
            MVCore = core
            MVInv = inv
            MVNP = np
        } else {
            throw IllegalArgumentException("Cannot initialize Multiverse")
        }
    }


}