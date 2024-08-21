/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.detomarco.kotlinfixture

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

/**
 * Returns an unmodifiable view of the specified map. Query operations on the returned map "read through" to the
 * specified map, and attempts to modify the returned map, whether direct or via its collection views, result in an
 * [UnsupportedOperationException].
 * @suppress
 */
fun <K, V> Map<K, V>.toUnmodifiableMap(): Map<K, V> = Collections.unmodifiableMap(this)

/**
 * Returns an unmodifiable view of the specified list. Query operations on the returned list "read through" to the
 * specified list, and attempts to modify the returned list, whether direct or via its iterator, result in an
 * [UnsupportedOperationException].
 * @suppress
 */
fun <T> List<T>.toUnmodifiableList(): List<T> = Collections.unmodifiableList(this)
