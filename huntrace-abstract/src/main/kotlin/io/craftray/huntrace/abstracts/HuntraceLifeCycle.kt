package io.craftray.huntrace.abstracts

import org.bukkit.plugin.Plugin

interface HuntraceLifeCycle {

    fun onLoad(plugin: Plugin) {
        LoadedSet.add(this)
    }

    fun onDestroy()
}