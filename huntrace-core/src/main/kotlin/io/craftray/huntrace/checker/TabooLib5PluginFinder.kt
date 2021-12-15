package io.craftray.huntrace.checker

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import org.bukkit.Bukkit
import java.util.logging.Logger

object TabooLib5PluginFinder {

    private val founds = Object2ObjectLinkedOpenHashMap<String, List<String>>()

    fun find() {
        Bukkit.getPluginManager().plugins.toSet().parallelStream()
            .filter { it::class.java.superclass?.name?.endsWith("boot.PluginBoot") == true }
            // Double check
            .filter {
                kotlin.runCatching { Class.forName("${it::class.java.packageName}/util/ILoader") }.isSuccess
            }
            .forEach { founds[it.name] = it.description.authors }
    }

    fun reportAllWithAuthors(logger: Logger, prefix: String = "") {
        founds.ifEmpty {
            logger.severe("$prefix[!] No TabooLib-5 based plugins found")
            logger.severe("$prefix[!] This could be because plugins haven't been loaded successfully")
            return
        }
        for ((name, authors) in founds) {
            logger.warning("$prefix$name authored by ${authors.joinToString()}")
        }
    }
}