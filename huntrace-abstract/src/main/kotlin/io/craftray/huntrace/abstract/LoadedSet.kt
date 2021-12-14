package io.craftray.huntrace.abstract

object LoadedSet : MutableSet<HuntraceLifeCycle> by mutableSetOf() {

    fun destroyAll() {
        forEach { kotlin.runCatching { it.onDestroy() }.onFailure { it.printStackTrace() } }
        clear()
    }
}