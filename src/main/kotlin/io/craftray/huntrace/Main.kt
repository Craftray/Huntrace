package io.craftray.huntrace

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin

object Main : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("config.yml")
    lateinit var config: SecuredFile

    override fun onEnable() {
        info("Hello Huntrace!")
    }

}