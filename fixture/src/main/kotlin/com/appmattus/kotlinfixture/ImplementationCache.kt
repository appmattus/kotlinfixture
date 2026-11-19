/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * The implementations of each abstract class or interface, found with [find] the first time each is asked for, and
 * cached, as finding them can take from microseconds to tens of milliseconds and the result does not change.
 *
 * Implementations found to be unusable are removed with [removeUnusable], so they are not tried again, and are left
 * out of lists found later for other abstract types. A list emptied this way, or found empty, is still cached.
 *
 * Only uses ConcurrentMap operations that all Android versions have: getOrPut relies on putIfAbsent, unlike
 * computeIfAbsent, and removal on replace.
 */
internal class ImplementationCache<T : Any>(private val find: (Class<*>) -> List<T>?) {

    private val implementations = ConcurrentHashMap<Class<*>, List<T>>()

    private val unusable: MutableSet<T> = Collections.newSetFromMap(ConcurrentHashMap())

    /**
     * The implementations of [clazz], or null if [find] does not know it. Two threads may both find the list, but only
     * the first is kept.
     */
    fun implementationsOf(clazz: Class<*>): List<T>? =
        implementations[clazz] ?: find(clazz)?.let { found ->
            // filterNot also copies the list, which may be mutable
            implementations.getOrPut(clazz) { found.filterNot { it in unusable } }
        }

    /**
     * Removes [implementation] from the implementations of [clazz], and leaves it out of lists found later
     */
    fun removeUnusable(clazz: Class<*>, implementation: T) {
        unusable += implementation

        while (true) {
            val current = implementations[clazz] ?: return
            if (implementation !in current || implementations.replace(clazz, current, current - implementation)) {
                return
            }
        }
    }
}
