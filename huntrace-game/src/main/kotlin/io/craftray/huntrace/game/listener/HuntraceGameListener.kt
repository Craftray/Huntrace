package io.craftray.huntrace.game.listener

import io.craftray.huntrace.game.Game
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class HuntraceGameListener : Listener {
    private var registered = false
    private var unregistered = false

    /**
     * Register this listener
     * @author Kylepoops
     * @exception IllegalStateException if this listener is already registered
     */
    fun register() {
        if (registered) throw IllegalStateException("Listener is already registered")
        Bukkit.getPluginManager().registerEvents(this, Game.plugin)
        this.registered = true
    }

    /**
     * Unregister this listener
     * @author Kylepoops
     * @exception IllegalStateException if this listener is not registered or have been unregistered
     */
    fun unregister() {
        if (!registered) throw IllegalStateException("Listener is not registered")
        if (unregistered) throw IllegalStateException("Listener is already unregistered")
        HandlerList.unregisterAll(this)
        this.unregistered = true
    }
}