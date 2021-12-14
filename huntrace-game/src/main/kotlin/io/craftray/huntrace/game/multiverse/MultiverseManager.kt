package io.craftray.huntrace.game.multiverse

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals
import com.onarandombox.multiverseinventories.MultiverseInventories
import io.craftray.huntrace.abstracts.HuntraceLifeCycle
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object MultiverseManager : HuntraceLifeCycle {
    lateinit var MVCore: MultiverseCore
    lateinit var MVInv: MultiverseInventories
    lateinit var MVNP: MultiverseNetherPortals

    /**
     * Initialize Multiverse Core, Multiverse Inventories, and Multiverse Nether Portals.
     * @author: Kylepoops
     */
    @Throws(IllegalArgumentException::class)
    private fun initMultiverse() {
        val core = Bukkit.getServer().pluginManager.getPlugin("Multiverse-Core") as? MultiverseCore?
        val inv = Bukkit.getServer().pluginManager.getPlugin("Multiverse-Inventories") as? MultiverseInventories?
        val np = Bukkit.getServer().pluginManager.getPlugin("Multiverse-NetherPortals") as? MultiverseNetherPortals?

        // check if multiverses are obtained properly
        if (core != null && inv != null && np != null) {
            MVCore = core
            MVInv = inv
            MVNP = np
        } else {
            throw IllegalArgumentException("Cannot initialize Multiverse")
        }
    }

    /**
     * Initialize Multiverse Core, Multiverse Inventories, and Multiverse Nether Portals.
     */
    override fun onLoad(plugin: Plugin) {
        super.onLoad(plugin)
        initMultiverse()
    }

    override fun onDestroy() = Unit
}