package io.craftray.huntrace

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.interaction.InteractionBase
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.util.logging.Level

object Main : Plugin() {

    private val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("config.yml")
    lateinit var config: Configuration

    override fun onEnable() {
        this.warnCompatibility()
        Game.init(plugin)
        InteractionBase.init(plugin)
    }

    @Suppress("TooGenericExceptionThrown")
    override fun onDisable() {
        // abort all running game and run all remain tasks in FreeTimeTaskScheduler
        Game.free()
        ExecutorHolder.awaitTerminationAll()
        // There's shouldn't be any tasks left, so check it again and throw an exception for each of them
        Bukkit.getScheduler().pendingTasks.filter { it.owner == plugin }.forEach {
            throw RuntimeException("Task still running: " + it.taskId)
        }
        // Double check
        Bukkit.getScheduler().cancelTasks(plugin)
        HandlerList.unregisterAll(plugin)
    }

    /**
     * Warn if the plugin is running on spigot, outdated java or outdated Minecraft versions
     */
    private fun warnCompatibility() {
        if (!isJava16AndAbove()) {
            Bukkit.getLogger().warning("---------------OUTDATED JAVA VERSION DETECTED---------------")
            plugin.logger.warning("You are running an outdated version of Java")
            plugin.logger.warning("Huntrace requires Java 16 or above")
            plugin.logger.warning("Previous version is not supported and probably run into issues")
            plugin.logger.warning("If you continue to use huntrace on this version, you will not get any support")
            plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("------------------------------------------------------------")
        }
        if (!PaperLib.isPaper()) {
            PaperLib.suggestPaper(plugin, Level.WARNING)
            Bukkit.getLogger().warning("---------------UNSUPPORTED SERVER VERSION DETECTED---------------")
            plugin.logger.warning("Some features will not work properly on Spigot")
            plugin.logger.warning("If you continue to use huntrace on Spigot, you will not get any support")
            plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
        if (!PaperLib.isVersion(17)) {
            Bukkit.getLogger().warning("---------------UNSUPPORTED SERVER VERSION DETECTED---------------")
            plugin.logger.warning("This plugin is designed for Paper 1.17 and above")
            plugin.logger.warning("Previous version is not supported and probably run into issues")
            plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
    }

    /**
     * Checks if the current Java version is at least Java 16
     * @return true if the current Java version is at least Java 16
     */
    private fun isJava16AndAbove() =
        kotlin.runCatching { Runtime.version() compareTo Runtime.Version.parse("16") >= 0 }.getOrDefault(false)
}