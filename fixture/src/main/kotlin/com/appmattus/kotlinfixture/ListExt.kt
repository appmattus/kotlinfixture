package com.appmattus.kotlinfixture

import java.util.Collections

internal fun <T> List<T>.circularIterator() = object : Iterator<T> {
    private var position = 0

    override fun hasNext() = isNotEmpty()

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        return get(position).also {
            position = ++position % size
        }
    }
}

fun <K, V> Map<K, V>.toUnmodifiableMap(): Map<K, V> = Collections.unmodifiableMap(this)

fun <T> List<T>.toUnmodifiableList(): List<T> = Collections.unmodifiableList(this)
