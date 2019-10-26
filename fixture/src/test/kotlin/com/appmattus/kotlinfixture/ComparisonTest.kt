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

        @Parameterized.Parameter(1)
        lateinit var resultClass: KClass<*>

        @Parameterized.Parameter(2)
        lateinit var appmattusSupports: Result
        @Parameterized.Parameter(3)
        lateinit var flextradeSupports: Result
        @Parameterized.Parameter(4)
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
            @Suppress("EXPERIMENTAL_API_USAGE")
            @Parameterized.Parameters(name = "{1}")
            fun data() = arrayOf(
                // Boolean
                arrayOf(typeOf<Boolean>(), Boolean::class, VALID, VALID, VALID),

                // Primitives and numbers
                arrayOf(typeOf<Byte>(), Byte::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<Double>(), Double::class, VALID, VALID, VALID),
                arrayOf(typeOf<Float>(), Float::class, VALID, VALID, VALID),
                arrayOf(typeOf<Int>(), Int::class, VALID, VALID, VALID),
                arrayOf(typeOf<Long>(), Long::class, VALID, VALID, VALID),
                arrayOf(typeOf<Short>(), Short::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UByte>(), UByte::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UInt>(), UInt::class, VALID, VALID, VALID),
                arrayOf(typeOf<ULong>(), ULong::class, VALID, VALID, VALID),
                arrayOf(typeOf<UShort>(), UShort::class, VALID, VALID, UNSUPPORTED),

                // String
                arrayOf(typeOf<String>(), String::class, VALID, VALID, VALID),

                // UUID
                arrayOf(typeOf<UUID>(), UUID::class, VALID, VALID, VALID),

                // Character
                arrayOf(typeOf<Char>(), Char::class, VALID, VALID, VALID),

                // Date
                arrayOf(typeOf<Date>(), Date::class, VALID, VALID, VALID),
                arrayOf(typeOf<Calendar>(), Calendar::class, VALID, VALID, UNSUPPORTED),

                // Tuples
                //arrayOf(typeOf<Pair<String, String>>(), Pair::class, VALID, UNSUPPORTED, VALID),
                //arrayOf(typeOf<Triple<String, String, String>>(), Triple::class, VALID, UNSUPPORTED, VALID),

                // Array
                arrayOf(typeOf<ByteArray>(), ByteArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<CharArray>(), CharArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<ShortArray>(), ShortArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<IntArray>(), IntArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<LongArray>(), LongArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<FloatArray>(), FloatArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<DoubleArray>(), DoubleArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<BooleanArray>(), BooleanArray::class, VALID, VALID, UNSUPPORTED),
                arrayOf(typeOf<UByteArray>(), UByteArray::class, VALID, VALID, VALID),
                arrayOf(typeOf<UIntArray>(), UIntArray::class, VALID, VALID, VALID),
                arrayOf(typeOf<ULongArray>(), ULongArray::class, VALID, VALID, VALID),
                arrayOf(typeOf<UShortArray>(), UShortArray::class, VALID, VALID, VALID),
                arrayOf(typeOf<Array<String>>(), Array<String>::class, VALID, VALID, UNSUPPORTED),

                // Iterable, List
                arrayOf(typeOf<Iterable<String>>(), Iterable::class, VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<Collection<String>>(), Collection::class, VALID, NOT_RANDOM, VALID),
                arrayOf(
                    typeOf<AbstractCollection<String>>(),
                    java.util.AbstractCollection::class,
                    VALID,
                    UNSUPPORTED,
                    UNSUPPORTED
                ),

                // Set
                arrayOf(typeOf<Set<String>>(), Set::class, VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<AbstractSet<String>>(), java.util.AbstractSet::class, VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<SortedSet<String>>(), SortedSet::class, VALID, INVALID, VALID),
                arrayOf(typeOf<NavigableSet<String>>(), NavigableSet::class, VALID, INVALID, VALID),
                arrayOf(typeOf<HashSet<String>>(), HashSet::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<LinkedHashSet<String>>(), LinkedHashSet::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<TreeSet<String>>(), TreeSet::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(
                    typeOf<ConcurrentSkipListSet<String>>(),
                    ConcurrentSkipListSet::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),
                arrayOf(
                    typeOf<CopyOnWriteArraySet<String>>(),
                    CopyOnWriteArraySet::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),

                // List
                arrayOf(typeOf<List<String>>(), List::class, VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<MutableList<String>>(), MutableList::class, VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<AbstractList<String>>(), java.util.AbstractList::class, VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<ArrayList<String>>(), ArrayList::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(
                    typeOf<AbstractSequentialList<String>>(),
                    AbstractSequentialList::class,
                    VALID,
                    UNSUPPORTED,
                    UNSUPPORTED
                ),
                arrayOf(typeOf<LinkedList<String>>(), LinkedList::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<Vector<String>>(), Vector::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<Stack<String>>(), Stack::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(
                    typeOf<CopyOnWriteArrayList<String>>(),
                    CopyOnWriteArrayList::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),

                // Queue
                arrayOf(typeOf<Queue<String>>(), Queue::class, VALID, VALID, VALID),
                arrayOf(typeOf<AbstractQueue<String>>(), AbstractQueue::class, VALID, UNSUPPORTED, UNSUPPORTED),
                arrayOf(typeOf<ConcurrentLinkedQueue<String>>(), ConcurrentLinkedQueue::class, VALID, VALID, VALID),
                arrayOf(typeOf<PriorityQueue<String>>(), PriorityQueue::class, VALID, VALID, VALID),
                //arrayOf(typeOf<DelayQueue<TestDelayed>>(), DelayQueue::class, VALID, VALID, VALID),
                arrayOf(typeOf<LinkedBlockingQueue<String>>(), LinkedBlockingQueue::class, VALID, VALID, VALID),
                arrayOf(typeOf<PriorityBlockingQueue<String>>(), PriorityBlockingQueue::class, VALID, VALID, VALID),
                arrayOf(typeOf<LinkedTransferQueue<String>>(), LinkedTransferQueue::class, VALID, VALID, VALID),
                arrayOf(typeOf<BlockingQueue<String>>(), BlockingQueue::class, VALID, INVALID, VALID),
                arrayOf(typeOf<TransferQueue<String>>(), TransferQueue::class, VALID, INVALID, VALID),

                // Deque
                arrayOf(typeOf<Deque<String>>(), Deque::class, VALID, VALID, VALID),
                arrayOf(typeOf<ArrayDeque<String>>(), ArrayDeque::class, VALID, VALID, VALID),
                arrayOf(typeOf<ConcurrentLinkedDeque<String>>(), ConcurrentLinkedDeque::class, VALID, VALID, VALID),
                arrayOf(typeOf<BlockingDeque<String>>(), BlockingDeque::class, VALID, INVALID, VALID),
                arrayOf(typeOf<LinkedBlockingDeque<String>>(), LinkedBlockingDeque::class, VALID, VALID, VALID),

                // Map
                arrayOf(typeOf<Map<String, String>>(), Map::class, VALID, NOT_RANDOM, VALID),
                arrayOf(typeOf<SortedMap<String, String>>(), SortedMap::class, VALID, INVALID, VALID),
                arrayOf(typeOf<NavigableMap<String, String>>(), NavigableMap::class, VALID, INVALID, VALID),
                arrayOf(typeOf<ConcurrentMap<String, String>>(), ConcurrentMap::class, VALID, INVALID, VALID),
                arrayOf(
                    typeOf<ConcurrentNavigableMap<String, String>>(),
                    ConcurrentNavigableMap::class,
                    VALID,
                    INVALID,
                    VALID
                ),
                arrayOf(
                    typeOf<java.util.AbstractMap<String, String>>(),
                    java.util.AbstractMap::class,
                    VALID,
                    UNSUPPORTED,
                    UNSUPPORTED
                ),
                arrayOf(typeOf<HashMap<String, String>>(), HashMap::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<LinkedHashMap<String, String>>(), LinkedHashMap::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(
                    typeOf<IdentityHashMap<String, String>>(),
                    IdentityHashMap::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),
                arrayOf(typeOf<WeakHashMap<String, String>>(), WeakHashMap::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(typeOf<TreeMap<String, String>>(), TreeMap::class, VALID, NOT_RANDOM, NOT_RANDOM),
                arrayOf(
                    typeOf<ConcurrentHashMap<String, String>>(),
                    ConcurrentHashMap::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),
                arrayOf(
                    typeOf<ConcurrentSkipListMap<String, String>>(),
                    ConcurrentSkipListMap::class,
                    VALID,
                    NOT_RANDOM,
                    NOT_RANDOM
                ),

                // Enum
                arrayOf(typeOf<TestEnumClass>(), TestEnumClass::class, VALID, VALID, UNSUPPORTED),

                // Class
                arrayOf(typeOf<TestClass>(), TestClass::class, VALID, VALID, VALID),

                // Object
                arrayOf(typeOf<TestObject>(), TestObject::class, NOT_RANDOM, UNSUPPORTED, NOT_RANDOM),

                // Sealed class
                arrayOf(typeOf<TestSealedClass>(), TestSealedClass::class, VALID, UNSUPPORTED, VALID),

                // Abstract class
                arrayOf(typeOf<Number>(), Number::class, VALID, UNSUPPORTED, UNSUPPORTED)

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
