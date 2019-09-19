package com.appmattus.kotlinfixture

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
