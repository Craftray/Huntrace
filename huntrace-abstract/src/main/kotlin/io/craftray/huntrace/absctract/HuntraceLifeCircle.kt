package io.craftray.huntrace.absctract

import org.bukkit.plugin.Plugin

val loaded = mutableSetOf<HuntraceLifeCircle>()

interface HuntraceLifeCircle {

    fun onLoad(plugin: Plugin) {
        loaded.add(this)
    }

    fun onDestroy()
}