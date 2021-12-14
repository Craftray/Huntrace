package io.craftray.huntrace.util.runnable

import io.craftray.huntrace.absctract.HuntraceLifeCycle
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * A wrapper of BukkitRunnable to submit tasks easily
 */
object BukkitRunnableWrapper : HuntraceLifeCycle {
    private lateinit var plugin: Plugin

    override fun onLoad(plugin: Plugin) {
        super.onLoad(plugin)
        this.plugin = plugin
    }

    @Suppress("TooGenericExceptionThrown")
    override fun onDestroy() {
        // There's shouldn't be any tasks left, so check it again and throw an exception for each of them
        Bukkit.getScheduler().pendingTasks.filter { it.owner == plugin }.forEach {
            throw RuntimeException("Task still running: " + it.taskId)
        }
        plugin.server.scheduler.cancelTasks(plugin)
    }

    fun submitDelayed(delay: Long, runnable: Runnable) =
        plugin.server.scheduler.runTaskLater(plugin, runnable, delay)

    fun submitDelayedAsync(delay: Long, runnable: Runnable) =
        plugin.server.scheduler.runTaskLaterAsynchronously(plugin, runnable, delay)

    fun submit(runnable: Runnable) =
        plugin.server.scheduler.runTask(plugin, runnable)

    fun submitTimer(delay: Long, interval: Long, runnable: Runnable) =
        plugin.server.scheduler.runTaskTimer(plugin, runnable, delay, interval)

    fun submitTimerAsync(delay: Long, interval: Long, runnable: Runnable) =
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, interval)
}