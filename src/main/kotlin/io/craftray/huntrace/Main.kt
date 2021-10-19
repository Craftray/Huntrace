package io.craftray.huntrace

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object Main : Plugin() {

    override fun onEnable() {
        info("Hello Huntrace!")
    }
}