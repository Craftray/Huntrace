package io.craftray.huntrace.game.scheduler

import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.game.Game
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

object FreeTimeTaskScheduler {
    private val tasks = ConcurrentLinkedQueue<Runnable>()
    private var initialized = false
    private var start = false
    private lateinit var thread: Thread

    @SubscribeEvent
    fun onPlayerQuit(@Suppress("UnusedPrivateMember") event: PlayerQuitEvent) {
        if (Bukkit.getOnlinePlayers().filterNot { it == event.player }.isEmpty()) {
            start = true
        }
    }

    @SubscribeEvent
    fun onPlayerJoin(@Suppress("UnusedPrivateMember") event: PlayerJoinEvent) {
        start = false
    }

    fun init() {
        check(!initialized) { "FreeTimeTaskScheduler is already initialized" }
        thread = thread(true) {
            while (!Thread.interrupted()) {
                if (start) bukkitRunnableOf { tasks.poll().run() }.runTask(Game.plugin)
            }
        }
        initialized = true
    }

    fun schedule(task: Runnable) = tasks.add(task)

    fun forceRun() {
        thread.interrupt()
        tasks.forEach(Runnable::run)
        tasks.clear()
    }
}