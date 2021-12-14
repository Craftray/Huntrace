package io.craftray.huntrace.absctract

import org.bukkit.plugin.Plugin

interface HuntraceLifeCycle {

    fun onLoad(plugin: Plugin) {
        LoadedSet.add(this)
    }

    fun onDestroy()
}