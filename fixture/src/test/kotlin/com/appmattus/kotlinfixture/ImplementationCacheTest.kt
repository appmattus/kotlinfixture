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

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class ImplementationCacheTest {

    private interface Known
    private interface Empty
    private interface Unknown

    private val finds = AtomicInteger()

    private val cache = ImplementationCache { clazz ->
        finds.incrementAndGet()
        when (clazz) {
            Known::class.java -> listOf("a", "b", "c")
            Empty::class.java -> emptyList()
            else -> null
        }
    }

    @Test
    fun `implementations are found once and cached`() {
        val first = cache.implementationsOf(Known::class.java)

        assertSame(first, cache.implementationsOf(Known::class.java))
        assertEquals(listOf("a", "b", "c"), first)
        assertEquals(1, finds.get())
    }

    @Test
    fun `an empty list is cached and not found again`() {
        assertEquals(emptyList(), cache.implementationsOf(Empty::class.java))
        assertEquals(emptyList(), cache.implementationsOf(Empty::class.java))

        assertEquals(1, finds.get())
    }

    @Test
    fun `an unknown class has no implementations`() {
        assertNull(cache.implementationsOf(Unknown::class.java))
    }

    @Test
    fun `the cached list is a copy`() {
        val found = mutableListOf("a", "b")
        val copyingCache = ImplementationCache { found }

        val implementations = copyingCache.implementationsOf(Known::class.java)
        found += "c"

        assertEquals(listOf("a", "b"), implementations)
    }

    @Test
    fun `unusable implementations are removed`() {
        cache.implementationsOf(Known::class.java)

        cache.removeUnusable(Known::class.java, "b")

        assertEquals(listOf("a", "c"), cache.implementationsOf(Known::class.java))
    }

    @Test
    fun `a list emptied by removing unusable implementations is cached and not found again`() {
        cache.implementationsOf(Known::class.java)

        listOf("a", "b", "c").forEach { cache.removeUnusable(Known::class.java, it) }

        assertEquals(emptyList(), cache.implementationsOf(Known::class.java))
        assertEquals(emptyList(), cache.implementationsOf(Known::class.java))
        assertEquals(1, finds.get())
    }

    @Test
    fun `unusable implementations are left out of lists found later`() {
        cache.removeUnusable(Empty::class.java, "b")

        assertEquals(listOf("a", "c"), cache.implementationsOf(Known::class.java))
    }

    @Test
    fun `concurrent removals are all applied`() {
        val items = List(ITEMS) { "item$it" }
        val concurrentCache = ImplementationCache { items }
        concurrentCache.implementationsOf(Known::class.java)

        val executor = Executors.newFixedThreadPool(THREADS)
        try {
            val start = CountDownLatch(1)
            val tasks = items.map { item ->
                executor.submit {
                    start.await()
                    concurrentCache.removeUnusable(Known::class.java, item)
                }
            }
            start.countDown()
            tasks.forEach { it.get() }
        } finally {
            executor.shutdown()
        }

        assertEquals(emptyList(), concurrentCache.implementationsOf(Known::class.java))
    }

    private companion object {
        private const val ITEMS = 200
        private const val THREADS = 16
    }
}
