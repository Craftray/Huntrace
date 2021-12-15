package io.craftray.huntrace.game.scheduler

import io.craftray.huntrace.abstract.HuntraceLifeCycle
import io.craftray.huntrace.util.runnable.BukkitRunnableWrapper
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import taboolib.common.platform.event.SubscribeEvent
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * Scheduler prepared for some hard works to prevent lag as much as possible.<br\>
 * It will check player count whenever a player is joining or leaving the server.<br\>
 * If the server has not server, it will start to prepare the tasks.
 */
@Suppress("unused")
object FreeTimeTaskScheduler : HuntraceLifeCycle {
    private val tasks = ConcurrentLinkedQueue<Runnable>()
    private var initialized = false
    private var start = false
    private lateinit var thread: Thread

    @SubscribeEvent
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.getOnlinePlayers().filterNot { it == event.player }.ifEmpty { start = true }
    }

    @SubscribeEvent
    fun onPlayerJoin(@Suppress("UnusedPrivateMember") event: PlayerJoinEvent) {
        start = false
    }

    fun schedule(task: Runnable) = tasks.add(task)

    private fun forceRunAndStop() {
        thread.interrupt()
        tasks.forEach(Runnable::run)
        tasks.clear()
    }

    override fun onLoad(plugin: Plugin) {
        super.onLoad(plugin)
        check(!initialized) { "FreeTimeTaskScheduler is already initialized" }
        thread = thread(true) {
            while (!Thread.interrupted()) {
                if (start && tasks.isNotEmpty()) BukkitRunnableWrapper.submit { tasks.poll().run() }
            }
        }
        initialized = true
    }

    override fun onDestroy() = forceRunAndStop()
}