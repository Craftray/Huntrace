package io.craftray.huntrace

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.platform.BukkitPlugin

object Main : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        info("Hello Huntrace!")
    }
}