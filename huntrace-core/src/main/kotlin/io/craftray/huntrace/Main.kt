package io.craftray.huntrace

import io.craftray.huntrace.game.Game
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin

object Main : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("config.yml")
    lateinit var config: SecuredFile

    override fun onEnable() {
        Game.init(plugin)
    }
}