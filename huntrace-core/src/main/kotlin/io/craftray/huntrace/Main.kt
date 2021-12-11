package io.craftray.huntrace

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.interaction.InteractionBase
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin

object Main : Plugin() {

    private val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("config.yml")
    lateinit var config: SecuredFile

    override fun onEnable() {
        Game.init(plugin)
        InteractionBase.init(plugin)
    }

    override fun onDisable() {
        Game.runningGame.forEach(Game::abort)
    }
}