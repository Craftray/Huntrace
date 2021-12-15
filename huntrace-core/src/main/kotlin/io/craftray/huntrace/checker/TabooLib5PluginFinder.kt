package io.craftray.huntrace.checker

import org.bukkit.Bukkit
import java.util.logging.Logger

object TabooLib5PluginFinder {

    private val founds = mutableMapOf<String, List<String>>()

    fun find() {
        Bukkit.getPluginManager().plugins.toSet().parallelStream()
            .filter { it::class.java.superclass?.name?.endsWith("boot.PluginBoot") == true }
            // Double check
            .filter {
                kotlin.runCatching {
                    Class.forName("${it::class.java.packageName}/util/ILoader")
                }.isSuccess
            }
            .forEach { founds[it.name] = it.description.authors }
    }

    fun reportAllWithAuthors(logger: Logger, prefix: String = "") {
        check(founds.isNotEmpty()) { "No TabooLib-5 relevant plugin is found" }
        for ((name, authors) in founds) {
            logger.warning("$prefix$name authored by ${authors.joinToString()}")
        }
    }
}