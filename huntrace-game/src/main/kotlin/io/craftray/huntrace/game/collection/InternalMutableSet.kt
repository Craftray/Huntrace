package io.craftray.huntrace.game.collection

/**
 * A set that is backed by another mutable set but can be modified only in this module
 * @author Kylepoops
 */
@Suppress("unused")
class InternalMutableSet<T> private constructor(private val delegate: MutableSet<T>) : Set<T> by delegate {

    constructor() : this(mutableSetOf())

    @JvmName("-add")
    internal fun add(element: T) {
        delegate.add(element)
    }

    @JvmName("-addAll")
    internal fun addAll(elements: Collection<T>) {
        delegate.addAll(elements)
    }

    @JvmName("-remove")
    internal fun remove(element: T) {
        delegate.remove(element)
    }

    @Suppress("ConvertArgumentToSet")
    @JvmName("-removeAll")
    internal fun removeAll(elements: Collection<T>) {
        delegate.removeAll(elements)
    }
}