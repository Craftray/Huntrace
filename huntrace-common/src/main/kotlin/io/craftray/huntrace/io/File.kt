package io.craftray.huntrace.io

import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.Future

val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())!!

/**
 * Delete the directory and all its contents asynchronously.<br/>
 * if you need to wait for the deletion to complete, pass in a set here and use Set#forEach(Future<*>::get).
 * @param await whether to wait for the deletion to complete
 * @param futures the future set to store future in
 */
fun File.deepDeleteAsync(await: Boolean = false, futures: MutableSet<Future<*>> = mutableSetOf()) {
    // first submit the task and get the future
    val future = executor.submit {
        if (this.exists()) {
            if (this.isDirectory) {
                // Construct another future set here
                // Because we need all the subdirectories and files to be deleted before this directory is deleted
                val thisFutures = mutableSetOf<Future<*>>()
                // Pass the future set to this, so we can store all the future
                this.listFiles()?.forEach { it.deepDeleteAsync(futures = thisFutures) }
                // Wait for all the subdirectories and files to be deleted
                thisFutures.forEach(Future<*>::get)
            }
            // Finally, delete this file or directory
            this.delete()
        }
    }
    // Add the future to the future set
    futures.add(future)
    // Wait the task to finish before returning if await is true
    // It shouldn't be called inside this function
    if (await) future.get()
}