/*
 * Copyright 2019 Appmattus Limited
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

import com.appmattus.kotlinfixture.ComparisonTest.Parameterised.Result.INVALID
import com.appmattus.kotlinfixture.ComparisonTest.Parameterised.Result.NOT_RANDOM
import com.appmattus.kotlinfixture.ComparisonTest.Parameterised.Result.UNSUPPORTED
import com.appmattus.kotlinfixture.ComparisonTest.Parameterised.Result.VALID
import com.appmattus.kotlinfixture.resolver.IterableKTypeResolverTest.Parameterised.TestDelayed
import com.flextrade.kfixture.KFixture
import com.marcellogalhardo.fixture.Fixture
import org.junit.Assume.assumeTrue
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.AbstractCollection
import java.util.AbstractList
import java.util.AbstractQueue
import java.util.AbstractSequentialList
import java.util.AbstractSet
import java.util.ArrayDeque
import java.util.Calendar
import java.util.Date
import java.util.Deque
import java.util.IdentityHashMap
import java.util.LinkedList
import java.util.NavigableMap
import java.util.NavigableSet
import java.util.PriorityQueue
import java.util.Queue
import java.util.SortedMap
import java.util.SortedSet
import java.util.Stack
import java.util.TreeMap
import java.util.TreeSet
import java.util.UUID
import java.util.Vector
import java.util.WeakHashMap
import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.DelayQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TransferQueue
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ComparisonTest {

    @RunWith(Parameterized::class)
    class Parameterised {
        @Parameterized.Parameter(0)
        lateinit var type: KType

        private val resultClass: KClass<*>
            get() = type.classifier as KClass<*>

        @Parameterized.Parameter(1)
        lateinit var appmattusSupports: Result
        @Parameterized.Parameter(2)
        lateinit var flextradeSupports: Result
        @Parameterized.Parameter(3)
        lateinit var marcellogalhardoSupports: Result

        private val marcellogalhardo = Fixture()
        private val flextrade = KFixture()
        private val appmattus = kotlinFixture()

        private fun assumeValid(result: Result) {
            assumeTrue(result == VALID || result == NOT_RANDOM)
        }

        private fun assumeRandom(result: Result) {
            assumeTrue(result == VALID)
        }

        private fun assumeInvalid(result: Result) {
            assumeTrue(result == INVALID)
        }

        private fun assumeUnsupported(result: Result) {
            assumeTrue(result == UNSUPPORTED)
        }

        @Test
        fun `appmattus creates instance`() {
            assumeValid(appmattusSupports)

            val result = appmattus.create(type, appmattus.fixtureConfiguration)!!

            println(result)
            println("Expected: $resultClass")
            println("Actual: ${result::class}")

            assertTrue {
                resultClass.isInstance(result)
            }
        }

        @Test
        fun `appmattus creates random instance`() {
            assumeRandom(appmattusSupports)

            assertIsRandom {
                appmattus.create(type, appmattus.fixtureConfiguration)!!
            }
        }

        @Test
        fun `flextrade creates instance`() {
            assumeValid(flextradeSupports)

            val result = flextrade.jFixture.create((type.classifier as KClass<*>).java)

            println(result)
            println("Expected: $resultClass")
            println("Actual: ${result::class}")

            assertTrue {
                resultClass.isInstance(result)
            }
        }

        @Test
        fun `flextrade creates random instance`() {
            assumeRandom(flextradeSupports)

            assertIsRandom {
                flextrade.jFixture.create((type.classifier as KClass<*>).java)
            }
        }

        @Test
        fun `flextrade cannot create instance`() {
            assumeUnsupported(flextradeSupports)

            assertFails {
                flextrade.jFixture.create((type.classifier as KClass<*>).java)
            }
        }

        @Test
        fun `flextrade creates invalid instance`() {
            assumeInvalid(flextradeSupports)

            val result = flextrade.jFixture.create((type.classifier as KClass<*>).java)

            assertFalse {
                resultClass.isInstance(result)
            }
        }

        @Test
        fun `marcellogalhardo creates instance`() {
            assumeValid(marcellogalhardoSupports)

            val result = marcellogalhardo.next(resultClass, type)!!

            assertTrue {
                resultClass.isInstance(result)
            }
        }

        @Test
        fun `marcellogalhardo creates random instance`() {
            assumeRandom(marcellogalhardoSupports)

            assertIsRandom {
                marcellogalhardo.next(resultClass, type)!!
            }
        }

        @Test
        fun `marcellogalhardo cannot create instance`() {
            assumeUnsupported(marcellogalhardoSupports)

            assertFails {
                marcellogalhardo.next(resultClass, type)!!
            }
        }

        @Test
        fun `marcellogalhardo creates invalid instance`() {
            assumeInvalid(marcellogalhardoSupports)

            val result = marcellogalhardo.next(resultClass, type)!!

            assertFalse {
                resultClass.isInstance(result)
            }
        }

        enum class Result {
            VALID, NOT_RANDOM, INVALID, UNSUPPORTED
        }

        companion object {
            @JvmStatic
            @Suppress("EXPERIMENTAL_API_USAGE", "LongMethod")
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                // Boolean
                arrayOf(typeOf<Boolean>(), VALID, VALID, VALID),

                // Primitives and numbers
                arrayOf(typeOf<Byte>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<Double>(), VALID, VALID, VALID),
                arrayOf(typeOf<Float>(), VALID, VALID, VALID),
                arrayOf(typeOf<Int>(), VALID, VALID, VALID),
                arrayOf(typeOf<Long>(), VALID, VALID, VALID),
                arrayOf(typeOf<Short>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UByte>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UInt>(), VALID, VALID, VALID),
                arrayOf(typeOf<ULong>(), VALID, VALID, VALID),
                arrayOf(typeOf<UShort>(), VALID, VALID, UNSUPPORTED),

                // String
                arrayOf(typeOf<String>(), VALID, VALID, VALID),

                // UUID
                arrayOf(typeOf<UUID>(), VALID, VALID, VALID),

                // Character
                arrayOf(typeOf<Char>(), VALID, VALID, VALID),

                // Date
                arrayOf(typeOf<Date>(), VALID, VALID, VALID),
                arrayOf(typeOf<Calendar>(), VALID, VALID, UNSUPPORTED),

                // Tuples
                arrayOf(typeOf<Pair<String, String>>(), VALID, UNSUPPORTED, VALID),
                arrayOf(typeOf<Triple<String, String, String>>(), VALID, UNSUPPORTED, VALID),

                // Array
                arrayOf(typeOf<ByteArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<CharArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<ShortArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<IntArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<LongArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<FloatArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<DoubleArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<BooleanArray>(), VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UByteArray>(), VALID, VALID, VALID),
                arrayOf(typeOf<UIntArray>(), VALID, VALID, VALID),
                arrayOf(typeOf<ULongArray>(), VALID, VALID, VALID),
                arrayOf(typeOf<UShortArray>(), VALID, VALID, VALID),
                arrayOf(typeOf<Array<String>>(), VALID, VALID, UNSUPPORTED),

                // Iterable, List
                arrayOf(typeOf<Iterable<String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<Collection<String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<AbstractCollection<String>>(), VALID, UNSUPPORTED, UNSUPPORTED),

                // Set
                arrayOf(typeOf<Set<String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<AbstractSet<String>>(), VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<SortedSet<String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<NavigableSet<String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<HashSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<LinkedHashSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<TreeSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<ConcurrentSkipListSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<CopyOnWriteArraySet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),

                // List
                arrayOf(typeOf<List<String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<MutableList<String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<AbstractList<String>>(), VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<ArrayList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<AbstractSequentialList<String>>(), VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<LinkedList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<Vector<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<Stack<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<CopyOnWriteArrayList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM),

                // Queue
                arrayOf(typeOf<Queue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<AbstractQueue<String>>(), VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<ConcurrentLinkedQueue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<PriorityQueue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<DelayQueue<TestDelayed>>(), VALID, VALID, VALID),
                arrayOf(typeOf<LinkedBlockingQueue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<PriorityBlockingQueue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<LinkedTransferQueue<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<BlockingQueue<String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<TransferQueue<String>>(), VALID, INVALID, VALID),

                // Deque
                arrayOf(typeOf<Deque<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<ArrayDeque<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<ConcurrentLinkedDeque<String>>(), VALID, VALID, VALID),
                arrayOf(typeOf<BlockingDeque<String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<LinkedBlockingDeque<String>>(), VALID, VALID, VALID),

                // Map
                arrayOf(typeOf<Map<String, String>>(), VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<SortedMap<String, String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<NavigableMap<String, String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<ConcurrentMap<String, String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<ConcurrentNavigableMap<String, String>>(), VALID, INVALID, VALID),
                arrayOf(typeOf<java.util.AbstractMap<String, String>>(), VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<HashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<LinkedHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<IdentityHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<WeakHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<TreeMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<ConcurrentHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<ConcurrentSkipListMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM),

                // Enum
                arrayOf(typeOf<TestEnumClass>(), VALID, VALID, UNSUPPORTED),

                // Class
                arrayOf(typeOf<TestClass>(), VALID, VALID, VALID),

                // Object
                arrayOf(typeOf<TestObject>(), NOT_RANDOM, UNSUPPORTED, NOT_RANDOM),

                // Sealed class
                arrayOf(typeOf<TestSealedClass>(), VALID, UNSUPPORTED, VALID),

                // Abstract class
                arrayOf(typeOf<Number>(), VALID, UNSUPPORTED, UNSUPPORTED)

                // Nullability
            )
        }
    }
}

object TestObject

sealed class TestSealedClass {
    class A : TestSealedClass()
    class B : TestSealedClass()
}

enum class TestEnumClass {
    A, B
}

data class TestClass(val value: String)
