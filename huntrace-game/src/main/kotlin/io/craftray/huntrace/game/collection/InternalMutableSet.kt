package io.craftray.huntrace.game.collection

/**
 * A set that is backed by another mutable set but can be modified only in this module
 * @author Kylepoops
 */
class InternalMutableSet<T> : Set<T> {
    private val _set = mutableSetOf<T>()

    override val size: Int
        get() = _set.size

    override operator fun contains(element: T) = element in _set

    override fun isEmpty() = _set.isEmpty()

    override fun iterator() = _set.toSet().iterator()

    override fun containsAll(elements: Collection<T>) = _set.containsAll(elements)

    @JvmName("-();add")
    internal fun add(element: T) = _set.add(element)

    @JvmName("-();addAll")
    internal fun addAll(elements: Collection<T>) = _set.addAll(elements)

    @JvmName("-();remove")
    internal fun remove(element: T) = _set.remove(element)

    @Suppress("ConvertArgumentToSet")
    @JvmName("-();removeAll")
    internal fun removeAll(elements: Collection<T>) = _set.removeAll(elements)
}