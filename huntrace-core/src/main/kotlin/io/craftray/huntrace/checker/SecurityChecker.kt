package io.craftray.huntrace.checker

import io.craftray.huntrace.Main
import org.bukkit.Bukkit

object SecurityChecker {

    fun warnLog4j2Exploit() {
        if (Log4j2RceExploitDetector.detect()) {
            Bukkit.getLogger().severe("-------------------LOG4J2 RCE EXPLOIT DETECTED-------------------")
            Main.plugin.logger.severe("Your server are found to have critical RCE exploit")
            Main.plugin.logger.severe("This can make other people able to do even anything in your server")
            Main.plugin.logger.severe("This is because a log4j2 bug between version 2.0 to 2.15-RC1")
            Main.plugin.logger.severe("This bug is fixed in 2.15-RC2 and above")
            Main.plugin.logger.severe("Your server is running log4j2 version ${Log4j2RceExploitDetector.log4j2Version}")
            Main.plugin.logger.severe("Please do any of the flowing to prevent this exploit: ")
            Main.plugin.logger.severe("- Update server to 1.18.1 and above")
            Main.plugin.logger.severe("- Update server to fixed version provided by spigot, paper or other project")
            Main.plugin.logger.severe("- Follow the official tutorial published by Mojang: https://www.minecraft.net/article/important-message--security-vulnerability-java-edition")
            if (Log4j2RceExploitDetector.isJvmFlagAvailable) {
                Main.plugin.logger.severe("- Add JVM Flag \"-Dlog4j2.formatMsgNoLookups=true\" to the start shell")
            }
            Main.plugin.logger.severe("- Set system environment \"LOG4J_FORMAT_MSG_NO_LOOKUPS\" to \"false\"")
            Main.plugin.logger.severe("- Manually delete \"org/apache/logging/log4j/core/lookup/JndiLookup.class\" from the server jar file")
            Bukkit.getLogger().warning("-----------------------------------------------------------------")
        }
    }
}