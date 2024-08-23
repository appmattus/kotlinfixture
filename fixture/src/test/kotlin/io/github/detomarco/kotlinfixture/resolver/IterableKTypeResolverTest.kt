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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.config.Configuration
import io.github.detomarco.kotlinfixture.typeOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.AbstractQueue
import java.util.AbstractSequentialList
import java.util.ArrayDeque
import java.util.Deque
import java.util.LinkedList
import java.util.NavigableSet
import java.util.PriorityQueue
import java.util.Queue
import java.util.SortedSet
import java.util.Stack
import java.util.TreeSet
import java.util.Vector
import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.TransferQueue
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IterableKTypeResolverTest {

    @Nested
    inner class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(IterableKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<Collection<Number>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Random nullability returned`() {
            assertIsRandom {
                context.resolve(typeOf<Collection<String>?>()) == null
            }
        }

        @Test
        fun `Length matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Collection<String>>()) as Collection<String>

            assertEquals(3, result.size)
        }

        @Test
        fun `Length matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Collection<String>>()) as Collection<String>

            assertEquals(7, result.size)
        }
    }

    private val context = TestContext(
        Configuration(),
        CompositeResolver(
            IterableKTypeResolver(),
            StringResolver(),
            PrimitiveResolver(),
            KTypeResolver(),
            KFunctionResolver(),
            KNamedPropertyResolver(),
            ClassResolver()
        )
    )

    @ParameterizedTest
    @MethodSource("data")
    fun `creates instance`(type: KType, resultClass: KClass<*>) {
        val result = context.resolve(type)

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(type: KType) {
        assertIsRandom {
            (context.resolve(type) as MutableCollection<*>).toList()
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Uses seeded random`(type: KType) {
        val value1 = (context.seedRandom().resolve(type) as MutableCollection<*>).toList()
        val value2 = (context.seedRandom().resolve(type) as MutableCollection<*>).toList()

        assertEquals(value1, value2)
    }

    data class TestDelayed(val value: Int) : Delayed {
        override fun compareTo(other: Delayed?): Int = 0

        override fun getDelay(unit: TimeUnit): Long = 0
    }

    companion object {
        @JvmStatic
        fun data() = arrayOf(
            arrayOf(typeOf<Iterable<String>>(), Iterable::class),
            arrayOf(typeOf<Collection<String>>(), Collection::class),
            arrayOf(typeOf<java.util.AbstractCollection<String>>(), java.util.AbstractCollection::class),

            // Set
            arrayOf(typeOf<Set<String>>(), Set::class),
            arrayOf(typeOf<java.util.AbstractSet<String>>(), java.util.AbstractSet::class),
            arrayOf(typeOf<SortedSet<String>>(), SortedSet::class),
            arrayOf(typeOf<NavigableSet<String>>(), NavigableSet::class),
            arrayOf(typeOf<HashSet<String>>(), HashSet::class),
            arrayOf(typeOf<LinkedHashSet<String>>(), LinkedHashSet::class),
            arrayOf(typeOf<TreeSet<String>>(), TreeSet::class),
            arrayOf(typeOf<ConcurrentSkipListSet<String>>(), ConcurrentSkipListSet::class),
            arrayOf(typeOf<CopyOnWriteArraySet<String>>(), CopyOnWriteArraySet::class),

            // List
            arrayOf(typeOf<List<String>>(), List::class),
            arrayOf(typeOf<MutableList<String>>(), MutableList::class),
            arrayOf(typeOf<java.util.AbstractList<String>>(), java.util.AbstractList::class),
            arrayOf(typeOf<ArrayList<String>>(), ArrayList::class),
            arrayOf(typeOf<AbstractSequentialList<String>>(), AbstractSequentialList::class),
            arrayOf(typeOf<LinkedList<String>>(), LinkedList::class),
            arrayOf(typeOf<Vector<String>>(), Vector::class),
            arrayOf(typeOf<Stack<String>>(), Stack::class),
            arrayOf(typeOf<CopyOnWriteArrayList<String>>(), CopyOnWriteArrayList::class),

            // Queue
            arrayOf(typeOf<Queue<String>>(), Queue::class),
            arrayOf(typeOf<AbstractQueue<String>>(), AbstractQueue::class),
            arrayOf(typeOf<ConcurrentLinkedQueue<String>>(), ConcurrentLinkedQueue::class),
            arrayOf(typeOf<PriorityQueue<String>>(), PriorityQueue::class),
            arrayOf(typeOf<DelayQueue<TestDelayed>>(), DelayQueue::class),
            arrayOf(typeOf<LinkedBlockingQueue<String>>(), LinkedBlockingQueue::class),
            arrayOf(typeOf<PriorityBlockingQueue<String>>(), PriorityBlockingQueue::class),
            arrayOf(typeOf<LinkedTransferQueue<String>>(), LinkedTransferQueue::class),
            arrayOf(typeOf<BlockingQueue<String>>(), BlockingQueue::class),
            arrayOf(typeOf<TransferQueue<String>>(), TransferQueue::class),

            // Deque
            arrayOf(typeOf<Deque<String>>(), Deque::class),
            arrayOf(typeOf<ArrayDeque<String>>(), ArrayDeque::class),
            arrayOf(typeOf<ConcurrentLinkedDeque<String>>(), ConcurrentLinkedDeque::class),
            arrayOf(typeOf<BlockingDeque<String>>(), BlockingDeque::class),
            arrayOf(typeOf<LinkedBlockingDeque<String>>(), LinkedBlockingDeque::class)
        )
    }
}
