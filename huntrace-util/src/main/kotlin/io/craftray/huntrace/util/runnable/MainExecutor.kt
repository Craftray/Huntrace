package io.craftray.huntrace.util.runnable

import io.craftray.huntrace.absctract.HuntraceLifeCircle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object MainExecutor : HuntraceLifeCircle, ExecutorService by Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())!! {

    override fun onDestroy() {
        awaitTermination(3, TimeUnit.SECONDS)
    }
}