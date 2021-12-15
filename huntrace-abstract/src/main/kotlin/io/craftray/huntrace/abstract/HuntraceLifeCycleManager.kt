package io.craftray.huntrace.abstract

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet

object HuntraceLifeCycleManager : MutableSet<HuntraceLifeCycle> by ObjectLinkedOpenHashSet() {

    fun destroyAll() {
        forEach { kotlin.runCatching { it.onDestroy() }.onFailure { it.printStackTrace() } }
        clear()
    }
}