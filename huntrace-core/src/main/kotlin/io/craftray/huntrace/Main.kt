package io.craftray.huntrace

import io.craftray.huntrace.abstract.HuntraceLifeCycleManager
import io.craftray.huntrace.checker.CompatibilityChecker
import io.craftray.huntrace.game.Game
import io.craftray.huntrace.interaction.InteractionBase
import io.craftray.huntrace.util.runnable.BukkitRunnableWrapper
import io.craftray.huntrace.util.runnable.MainExecutor
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin

@Suppress("unused")
object Main : Plugin() {

    internal val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("config.yml")
    lateinit var config: Configuration

    override fun onEnable() {
        CompatibilityChecker.warnCompatibility()
        MainExecutor.onLoad(plugin)
        BukkitRunnableWrapper.onLoad(plugin)
        Game.onLoad(plugin)
        InteractionBase.onLoad(plugin)
    }

    override fun onDisable() {
        // abort all loaded things
        HuntraceLifeCycleManager.destroyAll()
        // Double check
        Bukkit.getScheduler().cancelTasks(plugin)
        HandlerList.unregisterAll(plugin)
    }
}