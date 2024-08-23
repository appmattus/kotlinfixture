/*
 * Copyright 2021-2023 detomarco Limited
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

import com.flextrade.kfixture.KFixture
import com.marcellogalhardo.fixture.Fixture
import io.github.detomarco.kotlinfixture.ComparisonParametizedTest.Result.IGNORE
import io.github.detomarco.kotlinfixture.ComparisonParametizedTest.Result.INVALID
import io.github.detomarco.kotlinfixture.ComparisonParametizedTest.Result.NOT_RANDOM
import io.github.detomarco.kotlinfixture.ComparisonParametizedTest.Result.UNSUPPORTED
import io.github.detomarco.kotlinfixture.ComparisonParametizedTest.Result.VALID
import io.github.detomarco.kotlinfixture.resolver.IterableKTypeResolverTest
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.ktorm.entity.Entity
import java.net.URI
import java.net.URL
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.AbstractCollection
import java.util.AbstractList
import java.util.AbstractQueue
import java.util.AbstractSequentialList
import java.util.AbstractSet
import java.util.ArrayDeque
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Deque
import java.util.GregorianCalendar
import java.util.IdentityHashMap
import java.util.LinkedList
import java.util.Locale
import java.util.NavigableMap
import java.util.NavigableSet
import java.util.PriorityQueue
import java.util.Queue
import java.util.SortedMap
import java.util.SortedSet
import java.util.Stack
import java.util.TimeZone
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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicIntegerArray
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicLongArray
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UnusedParameter")
class ComparisonParametizedTest {

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

    @ParameterizedTest
    @MethodSource("data")
    fun `detomarco creates instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeValid(detomarcoSupports)

        @Suppress("DEPRECATION_ERROR")
        val result = detomarco.create(type, detomarco.fixtureConfiguration)!!

        // Special handling of primitive arrays due to bug in the Kotlin type system
        // See https://youtrack.jetbrains.com/issue/KT-52170/Reflection-typeOfArrayLong-gives-classifier-LongArray
        if (type in listOf(
                typeOf<Array<Boolean>>(),
                typeOf<Array<Byte>>(),
                typeOf<Array<Double>>(),
                typeOf<Array<Float>>(),
                typeOf<Array<Int>>(),
                typeOf<Array<Long>>(),
                typeOf<Array<Short>>(),
                typeOf<Array<Char>>()
            )
        ) {
            val argumentType = type.arguments[0].type?.classifier as KClass<*>
            assertTrue {
                result is Array<*>
            }
            (result as Array<*>).forEach {
                argumentType.isInstance(it)
            }
        } else {
            val resultClass = type.classifier as KClass<*>
            assertTrue {
                resultClass.isInstance(result)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `detomarco creates random instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeRandom(detomarcoSupports)

        assertIsRandom {
            @Suppress("DEPRECATION_ERROR")
            detomarco.create(type, detomarco.fixtureConfiguration)!!
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `flextrade creates instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeValid(flextradeSupports)

        val result = flextrade.jFixture.create((type.classifier as KClass<*>).java)
        val resultClass = type.classifier as KClass<*>
        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `flextrade creates random instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeRandom(flextradeSupports)

        assertIsRandom {
            flextrade.jFixture.create((type.classifier as KClass<*>).java)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `flextrade cannot create instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeUnsupported(flextradeSupports)

        assertFails {
            flextrade.jFixture.create((type.classifier as KClass<*>).java)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `flextrade creates invalid instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeInvalid(flextradeSupports)

        val result = flextrade.jFixture.create((type.classifier as KClass<*>).java)

        val resultClass = type.classifier as KClass<*>
        assertFalse {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `marcellogalhardo creates instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeValid(marcellogalhardoSupports)
        val resultClass = type.classifier as KClass<*>
        val result = marcellogalhardo.next(resultClass, type)!!

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `marcellogalhardo creates random instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeRandom(marcellogalhardoSupports)
        val resultClass = type.classifier as KClass<*>
        assertIsRandom {
            marcellogalhardo.next(resultClass, type)!!
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `marcellogalhardo cannot create instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeUnsupported(marcellogalhardoSupports)
        val resultClass = type.classifier as KClass<*>
        assertFails {
            marcellogalhardo.next(resultClass, type)!!
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `marcellogalhardo creates invalid instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeInvalid(marcellogalhardoSupports)
        val resultClass = type.classifier as KClass<*>
        val result = marcellogalhardo.next(resultClass, type)!!

        assertFalse {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `easyrandom creates instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeValid(easyRandomSupports)
        val resultClass = type.classifier as KClass<*>
        val result = easyRandom.nextObject(resultClass.java)

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `easyrandom creates random instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeRandom(easyRandomSupports)
        val resultClass = type.classifier as KClass<*>
        assertIsRandom {
            easyRandom.nextObject(resultClass.java)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `easyrandom cannot create instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeUnsupported(easyRandomSupports)
        val resultClass = type.classifier as KClass<*>
        assertFails {
            easyRandom.nextObject(resultClass.java)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `easyrandom creates invalid instance`(
        type: KType,
        detomarcoSupports: Result,
        flextradeSupports: Result,
        marcellogalhardoSupports: Result,
        easyRandomSupports: Result
    ) {
        assumeInvalid(easyRandomSupports)
        val resultClass = type.classifier as KClass<*>
        val result = easyRandom.nextObject(resultClass.java)

        assertFalse {
            resultClass.isInstance(result)
        }
    }

    enum class Result {
        VALID, NOT_RANDOM, INVALID, UNSUPPORTED, IGNORE
    }

    companion object {
        private val marcellogalhardo = Fixture()
        private val flextrade = KFixture()
        private val detomarco = kotlinFixture()
        private val easyRandom = EasyRandom()

        @OptIn(ExperimentalUnsignedTypes::class)
        @JvmStatic
        @Suppress("LongMethod")
        fun data() = arrayOf(
            // Boolean
            arrayOf(typeOf<Boolean>(), VALID, VALID, VALID, VALID),

            // Primitives and numbers
            arrayOf(typeOf<Byte>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Double>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Float>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Int>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Long>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Short>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<UByte>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<UInt>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<ULong>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<UShort>(), VALID, VALID, UNSUPPORTED, VALID),

            // String
            arrayOf(typeOf<String>(), VALID, VALID, VALID, VALID),

            // UUID
            arrayOf(typeOf<UUID>(), VALID, VALID, VALID, VALID),

            // Character
            arrayOf(typeOf<Char>(), VALID, VALID, VALID, VALID),

            // Date
            arrayOf(typeOf<Date>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Calendar>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<GregorianCalendar>(), VALID, UNSUPPORTED, VALID, VALID),
            arrayOf(typeOf<java.sql.Date>(), VALID, UNSUPPORTED, VALID, VALID),
            arrayOf(typeOf<java.sql.Time>(), VALID, UNSUPPORTED, VALID, VALID),
            arrayOf(typeOf<java.sql.Timestamp>(), VALID, VALID, VALID, VALID),

            arrayOf(typeOf<Instant>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<ZonedDateTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<LocalDate>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<LocalTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<LocalDateTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<OffsetDateTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<OffsetTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Duration>(), VALID, IGNORE, UNSUPPORTED, VALID),
            arrayOf(typeOf<Period>(), VALID, IGNORE, UNSUPPORTED, VALID),
            arrayOf(typeOf<ZoneId>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<ZoneOffset>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<TimeZone>(), VALID, UNSUPPORTED, UNSUPPORTED, VALID),
            arrayOf(typeOf<Year>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<Month>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<YearMonth>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<MonthDay>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),

            arrayOf(typeOf<org.threeten.bp.Instant>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.ZonedDateTime>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<org.threeten.bp.LocalDate>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.LocalTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.LocalDateTime>(), VALID, UNSUPPORTED, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.OffsetDateTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.OffsetTime>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.Duration>(), VALID, IGNORE, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.Period>(), VALID, IGNORE, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.ZoneId>(), VALID, NOT_RANDOM, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<org.threeten.bp.ZoneOffset>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.Year>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.Month>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.YearMonth>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),
            arrayOf(typeOf<org.threeten.bp.MonthDay>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),

            arrayOf(typeOf<org.joda.time.Instant>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<org.joda.time.LocalDate>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<org.joda.time.LocalTime>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<org.joda.time.LocalDateTime>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<org.joda.time.DateTime>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<org.joda.time.Duration>(), VALID, VALID, IGNORE, VALID),
            arrayOf(typeOf<org.joda.time.Period>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<org.joda.time.DateTimeZone>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<org.joda.time.Interval>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),

            // Tuples
            arrayOf(typeOf<Pair<String, String>>(), VALID, UNSUPPORTED, VALID, UNSUPPORTED),
            arrayOf(typeOf<Triple<String, String, String>>(), VALID, UNSUPPORTED, VALID, UNSUPPORTED),

            // Array
            arrayOf(typeOf<ByteArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<CharArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<ShortArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<IntArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<LongArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<FloatArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<DoubleArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<BooleanArray>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<UByteArray>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<UIntArray>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<ULongArray>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<UShortArray>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<Array<Boolean>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Byte>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Double>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Float>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Int>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Long>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Short>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<Char>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<UByte>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<UInt>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<ULong>>(), VALID, VALID, UNSUPPORTED, VALID),
            arrayOf(typeOf<Array<UShort>>(), VALID, VALID, UNSUPPORTED, VALID),

            // Iterable, List
            arrayOf(typeOf<Iterable<String>>(), VALID, NOT_RANDOM, VALID, UNSUPPORTED),
            arrayOf(typeOf<Collection<String>>(), VALID, NOT_RANDOM, VALID, NOT_RANDOM),
            arrayOf(typeOf<AbstractCollection<String>>(), VALID, UNSUPPORTED, UNSUPPORTED, NOT_RANDOM),

            // Set
            arrayOf(typeOf<Set<String>>(), VALID, NOT_RANDOM, VALID, NOT_RANDOM),
            arrayOf(typeOf<AbstractSet<String>>(), VALID, UNSUPPORTED, UNSUPPORTED, NOT_RANDOM),
            arrayOf(typeOf<SortedSet<String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<NavigableSet<String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<HashSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<LinkedHashSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<TreeSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<ConcurrentSkipListSet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<CopyOnWriteArraySet<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),

            // List
            arrayOf(typeOf<List<String>>(), VALID, NOT_RANDOM, VALID, NOT_RANDOM),
            arrayOf(typeOf<MutableList<String>>(), VALID, NOT_RANDOM, VALID, NOT_RANDOM),
            arrayOf(typeOf<AbstractList<String>>(), VALID, UNSUPPORTED, UNSUPPORTED, NOT_RANDOM),
            arrayOf(typeOf<ArrayList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<AbstractSequentialList<String>>(), VALID, UNSUPPORTED, UNSUPPORTED, INVALID),
            arrayOf(typeOf<LinkedList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<Vector<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<Stack<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<CopyOnWriteArrayList<String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),

            // Queue
            arrayOf(typeOf<Queue<String>>(), VALID, VALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<AbstractQueue<String>>(), VALID, UNSUPPORTED, UNSUPPORTED, INVALID),
            arrayOf(typeOf<ConcurrentLinkedQueue<String>>(), VALID, VALID, VALID, INVALID),
            arrayOf(typeOf<PriorityQueue<String>>(), VALID, VALID, VALID, INVALID),
            arrayOf(typeOf<DelayQueue<IterableKTypeResolverTest.TestDelayed>>(), VALID, VALID, VALID, INVALID),
            arrayOf(typeOf<LinkedBlockingQueue<String>>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<PriorityBlockingQueue<String>>(), VALID, VALID, VALID, INVALID),
            arrayOf(typeOf<LinkedTransferQueue<String>>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<BlockingQueue<String>>(), VALID, INVALID, VALID, VALID),
            arrayOf(typeOf<TransferQueue<String>>(), VALID, INVALID, VALID, VALID),

            // Deque
            arrayOf(typeOf<Deque<String>>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<ArrayDeque<String>>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<ConcurrentLinkedDeque<String>>(), VALID, VALID, VALID, INVALID),
            arrayOf(typeOf<BlockingDeque<String>>(), VALID, INVALID, VALID, VALID),
            arrayOf(typeOf<LinkedBlockingDeque<String>>(), VALID, VALID, VALID, VALID),

            // Map
            arrayOf(typeOf<Map<String, String>>(), VALID, NOT_RANDOM, VALID, NOT_RANDOM),
            arrayOf(typeOf<SortedMap<String, String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<NavigableMap<String, String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<ConcurrentMap<String, String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<ConcurrentNavigableMap<String, String>>(), VALID, INVALID, VALID, NOT_RANDOM),
            arrayOf(typeOf<java.util.AbstractMap<String, String>>(), VALID, UNSUPPORTED, UNSUPPORTED, NOT_RANDOM),
            arrayOf(typeOf<HashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<LinkedHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<IdentityHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<WeakHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, INVALID),
            arrayOf(typeOf<TreeMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<ConcurrentHashMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),
            arrayOf(typeOf<ConcurrentSkipListMap<String, String>>(), VALID, NOT_RANDOM, NOT_RANDOM, NOT_RANDOM),

            // Uris class
            arrayOf(typeOf<URI>(), VALID, NOT_RANDOM, VALID, VALID),
            arrayOf(typeOf<URL>(), VALID, NOT_RANDOM, UNSUPPORTED, VALID),

            // Format class
            arrayOf(typeOf<DateFormat>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<SimpleDateFormat>(), VALID, UNSUPPORTED, NOT_RANDOM, UNSUPPORTED),
            arrayOf(typeOf<NumberFormat>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<DecimalFormat>(), VALID, UNSUPPORTED, NOT_RANDOM, UNSUPPORTED),

            arrayOf(typeOf<Currency>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),
            arrayOf(typeOf<Locale>(), VALID, UNSUPPORTED, VALID, VALID),

            // Atomic class
            arrayOf(typeOf<AtomicBoolean>(), VALID, VALID, VALID, UNSUPPORTED),
            arrayOf(typeOf<AtomicInteger>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<AtomicLong>(), VALID, VALID, VALID, VALID),
            arrayOf(typeOf<AtomicIntegerArray>(), VALID, VALID, IGNORE, UNSUPPORTED),
            arrayOf(typeOf<AtomicLongArray>(), VALID, VALID, IGNORE, UNSUPPORTED),
            arrayOf(typeOf<AtomicReference<String>>(), VALID, UNSUPPORTED, VALID, UNSUPPORTED),

            // Enum
            arrayOf(typeOf<TestEnumClass>(), VALID, VALID, UNSUPPORTED, VALID),

            // Class
            arrayOf(typeOf<TestClass>(), VALID, VALID, VALID, VALID),

            // Object
            arrayOf(typeOf<TestObject>(), NOT_RANDOM, UNSUPPORTED, NOT_RANDOM, VALID),

            // Sealed class
            arrayOf(typeOf<TestSealedClass>(), VALID, UNSUPPORTED, VALID, UNSUPPORTED),

            // Abstract class
            arrayOf(typeOf<Number>(), VALID, UNSUPPORTED, UNSUPPORTED, UNSUPPORTED),

            // KTorm
            arrayOf(typeOf<Entity<KTorm>>(), VALID, UNSUPPORTED, IGNORE, UNSUPPORTED)
        )
    }
}

object TestObject

@Suppress("CanSealedSubClassBeObject")
sealed class TestSealedClass {
    @Suppress("unused", "CanSealedSubClassBeObject")
    class A : TestSealedClass()

    @Suppress("unused")
    class B : TestSealedClass()
}

@Suppress("unused")
enum class TestEnumClass {
    A, B
}

data class TestClass(val value: String)

interface KTorm : Entity<KTorm> {
    companion object : Entity.Factory<KTorm>()
}
