package io.craftray.huntrace.abstracts

import org.bukkit.Bukkit
import java.util.logging.Level

object LoadedSet : MutableSet<HuntraceLifeCycle> by mutableSetOf() {

    fun destroyAll() {
        forEach {
            kotlin.runCatching { it.onDestroy() }.onFailure {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot fully disable Huntrace", it)
            }
        }
        clear()
    }
}