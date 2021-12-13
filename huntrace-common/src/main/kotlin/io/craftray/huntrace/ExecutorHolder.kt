package io.craftray.huntrace

import java.util.concurrent.ExecutorService

object ExecutorHolder : MutableSet<ExecutorService> by mutableSetOf() {
    fun awaitTerminationAll() {
        forEach { it.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS) }
    }
}