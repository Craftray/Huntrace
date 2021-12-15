package io.craftray.huntrace.checker

import io.craftray.huntrace.Main
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import java.util.logging.Level

object CompatibilityChecker {
    /**
     * Warn if the plugin is running on spigot, outdated java or outdated Minecraft versions
     */
    internal fun warnCompatibility() {
        warnSpigotAndCraftBukkit()
        warnPreviousMinecraftVersion()
        warnTabooLib5()
    }

    private fun warnSpigotAndCraftBukkit() {
        if (!PaperLib.isPaper()) {
            PaperLib.suggestPaper(Main.plugin, Level.WARNING)
            Bukkit.getLogger().warning("---------------UNSUPPORTED SERVER VERSION DETECTED---------------")
            Main.plugin.logger.warning("Some features will not work properly on Spigot")
            Main.plugin.logger.warning("If you continue to use huntrace on Spigot, you will not get any support")
            Main.plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
    }

    private fun warnPreviousMinecraftVersion() {
        if (!PaperLib.isVersion(17)) {
            Bukkit.getLogger().warning("---------------UNSUPPORTED SERVER VERSION DETECTED---------------")
            Main.plugin.logger.warning("This plugin is designed for Paper 1.17 and above")
            Main.plugin.logger.warning("Previous version is not supported and probably run into issues")
            Main.plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
    }

    private fun warnTabooLib5() {
        kotlin.runCatching { Class.forName("io.izzel.taboolib.TabooLib") }.onSuccess {
            TabooLib5PluginFinder.find()
            Bukkit.getLogger().warning("---------------UNSUPPORTED ENVIRONMENT DETECTED---------------")
            Main.plugin.logger.warning("Huntrace is not compatible TabooLib-5 Hot-Loading System")
            Main.plugin.logger.warning("This system is supposed to have series of bugs")
            Main.plugin.logger.warning("This might lead to unexpected behaviours")
            Main.plugin.logger.warning("The following plugins are found to be using TabooLib-5:")
            TabooLib5PluginFinder.reportAllWithAuthors(Main.plugin.logger, prefix = "- ")
            Main.plugin.logger.warning("Please contact the plugin authors to update to TabooLib-6")
            Main.plugin.logger.warning("Continues to use with TabooLib-5 means no support will be provided")
            Main.plugin.logger.warning("YOU HAVE BEEN WARNED")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
    }
}