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
import org.junit.jupiter.params.provider.Arguments
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
            Arguments.of(typeOf<Iterable<String>>(), Iterable::class),
            Arguments.of(typeOf<Collection<String>>(), Collection::class),
            Arguments.of(typeOf<java.util.AbstractCollection<String>>(), java.util.AbstractCollection::class),

            // Set
            Arguments.of(typeOf<Set<String>>(), Set::class),
            Arguments.of(typeOf<java.util.AbstractSet<String>>(), java.util.AbstractSet::class),
            Arguments.of(typeOf<SortedSet<String>>(), SortedSet::class),
            Arguments.of(typeOf<NavigableSet<String>>(), NavigableSet::class),
            Arguments.of(typeOf<HashSet<String>>(), HashSet::class),
            Arguments.of(typeOf<LinkedHashSet<String>>(), LinkedHashSet::class),
            Arguments.of(typeOf<TreeSet<String>>(), TreeSet::class),
            Arguments.of(typeOf<ConcurrentSkipListSet<String>>(), ConcurrentSkipListSet::class),
            Arguments.of(typeOf<CopyOnWriteArraySet<String>>(), CopyOnWriteArraySet::class),

            // List
            Arguments.of(typeOf<List<String>>(), List::class),
            Arguments.of(typeOf<MutableList<String>>(), MutableList::class),
            Arguments.of(typeOf<java.util.AbstractList<String>>(), java.util.AbstractList::class),
            Arguments.of(typeOf<ArrayList<String>>(), ArrayList::class),
            Arguments.of(typeOf<AbstractSequentialList<String>>(), AbstractSequentialList::class),
            Arguments.of(typeOf<LinkedList<String>>(), LinkedList::class),
            Arguments.of(typeOf<Vector<String>>(), Vector::class),
            Arguments.of(typeOf<Stack<String>>(), Stack::class),
            Arguments.of(typeOf<CopyOnWriteArrayList<String>>(), CopyOnWriteArrayList::class),

            // Queue
            Arguments.of(typeOf<Queue<String>>(), Queue::class),
            Arguments.of(typeOf<AbstractQueue<String>>(), AbstractQueue::class),
            Arguments.of(typeOf<ConcurrentLinkedQueue<String>>(), ConcurrentLinkedQueue::class),
            Arguments.of(typeOf<PriorityQueue<String>>(), PriorityQueue::class),
            Arguments.of(typeOf<DelayQueue<TestDelayed>>(), DelayQueue::class),
            Arguments.of(typeOf<LinkedBlockingQueue<String>>(), LinkedBlockingQueue::class),
            Arguments.of(typeOf<PriorityBlockingQueue<String>>(), PriorityBlockingQueue::class),
            Arguments.of(typeOf<LinkedTransferQueue<String>>(), LinkedTransferQueue::class),
            Arguments.of(typeOf<BlockingQueue<String>>(), BlockingQueue::class),
            Arguments.of(typeOf<TransferQueue<String>>(), TransferQueue::class),

            // Deque
            Arguments.of(typeOf<Deque<String>>(), Deque::class),
            Arguments.of(typeOf<ArrayDeque<String>>(), ArrayDeque::class),
            Arguments.of(typeOf<ConcurrentLinkedDeque<String>>(), ConcurrentLinkedDeque::class),
            Arguments.of(typeOf<BlockingDeque<String>>(), BlockingDeque::class),
            Arguments.of(typeOf<LinkedBlockingDeque<String>>(), LinkedBlockingDeque::class)
        )
    }
}
