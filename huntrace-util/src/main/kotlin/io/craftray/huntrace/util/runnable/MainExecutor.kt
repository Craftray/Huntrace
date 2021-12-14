package io.craftray.huntrace.util.runnable

import io.craftray.huntrace.absctract.HuntraceLifeCycle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * The main thread poll executor of huntrace.<br\>
 * Should use [BukkitRunnableWrapper] in most cases
 */
object MainExecutor : HuntraceLifeCycle, ExecutorService by Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())!! {

    override fun onDestroy() {
        awaitTermination(3, TimeUnit.SECONDS)
    }
}