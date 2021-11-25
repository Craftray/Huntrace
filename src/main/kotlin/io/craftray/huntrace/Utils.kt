package io.craftray.huntrace

import org.bukkit.scheduler.BukkitRunnable

object Utils {
    fun bukkitRunnableOf(block: () -> Unit) = object : BukkitRunnable() {
        override fun run() {
            block()
        }
    }

    //return a random boolean with specific chance
    fun randomBoolean(chance: Float) = Math.random() < chance
}