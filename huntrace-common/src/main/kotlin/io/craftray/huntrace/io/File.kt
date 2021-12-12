package io.craftray.huntrace.io

import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.Future

val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())!!

fun File.deepDeleteAsync(await: Boolean = false, futures: MutableSet<Future<*>> = mutableSetOf()) {
    val future = executor.submit {
        if (this.exists()) {
            if (this.isDirectory) {
                val thisFutures = mutableSetOf<Future<*>>()
                this.listFiles()?.forEach { it.deepDeleteAsync(futures = thisFutures) }
                thisFutures.forEach(Future<*>::get)
            }
            this.delete()
        }
    }
    futures.add(future)
    if (await) future.get()
}