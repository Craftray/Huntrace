package io.craftray.huntrace.abstract

import org.bukkit.plugin.Plugin

@JvmDefaultWithoutCompatibility
interface HuntraceLifeCycle {

    fun onLoad(plugin: Plugin) {
        HuntraceLifeCycleManager.add(this)
    }

    fun onDestroy()
}