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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.config.before
import com.appmattus.kotlinfixture.kotlinFixture
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.MonthDay
import org.joda.time.Period
import org.joda.time.YearMonth
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class JodaTimeResolverTest {

    class Single {
        private val now = Date()

        private val context = TestContext(
            ConfigurationBuilder().apply { factory<Date> { before(now) } }.build(),
            CompositeResolver(JodaTimeResolver(), FactoryResolver(), KTypeResolver())
        )

        private val unconfiguredContext = TestContext(
            Configuration(),
            CompositeResolver(JodaTimeResolver(), KTypeResolver(), DateResolver())
        )

        @Test
        fun `Before specification gives YearMonth no later than now`() {
            // The latest current date anywhere, as values are generated in a random time zone
            val latestNow = YearMonth.now(DateTimeZone.forOffsetHours(MAX_OFFSET_HOURS))

            repeat(100) {
                val result = context.resolve(YearMonth::class) as YearMonth

                assertTrue("$result is after $latestNow") { !result.isAfter(latestNow) }
            }
        }

        @Test
        fun `YearMonth is realistic`() {
            repeat(100) {
                val result = unconfiguredContext.resolve(YearMonth::class) as YearMonth

                assertTrue("$result is not within ten years of 2020") { result.year in 2009..2030 }
            }
        }

        @Test
        fun `MonthDay generates every valid day of every month`() {
            val expected = (1..MONTHS_IN_YEAR).flatMap { month ->
                (1..MonthDay(month, 1).dayOfMonth().maximumValue).map { MonthDay(month, it) }
            }

            val generated = List(MONTH_DAY_SAMPLES) { unconfiguredContext.resolve(MonthDay::class) as MonthDay }.toSet()

            assertEquals(366, expected.size)
            assertEquals(expected.toSet() - generated, emptySet(), "Valid days never generated")
        }

        private companion object {
            private const val SEED = 7
            private const val MAX_OFFSET_HOURS = 18
            private const val MONTHS_IN_YEAR = 12

            // Enough samples for every one of the 366 month-days to be generated with near certainty
            private const val MONTH_DAY_SAMPLES = 20_000
        }

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Before specification gives date in the past`() {
            repeat(100) {
                val result = context.resolve(DateTime::class) as DateTime

                assertTrue {
                    result.toInstant() <= Instant(now)
                }
            }
        }

        @Test
        fun `Can override DateTimeZone when creating DateTime`() {
            repeat(100) {
                val fixture = kotlinFixture {
                    factory<DateTimeZone> { DateTimeZone.forID("Europe/London") }
                }
                val dateTime = fixture<DateTime>()

                assertEquals("Europe/London", dateTime.zone.id)
            }
        }

        @Test
        fun `seeded DateTimeZone is picked from the zones in sorted order`() {
            val fixture = kotlinFixture { random = Random(SEED) }

            assertEquals(
                DateTimeZone.forID(DateTimeZone.getAvailableIDs().sorted().random(Random(SEED))),
                fixture<DateTimeZone>()
            )
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        @Parameterized.Parameter(0)
        lateinit var type: KClass<*>

        @Suppress("UNCHECKED_CAST")
        private val context = TestContext(
            Configuration(),
            CompositeResolver(JodaTimeResolver(), KTypeResolver(), DateResolver(), EnumResolver())
        )

        @Test
        fun `Class returns date`() {
            repeat(100) {
                val result = context.resolve(type)

                assertNotNull(result)
                assertTrue {
                    type.isInstance(result)
                }
            }
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                context.resolve(type)
            }
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(Instant::class),
                arrayOf(LocalDate::class),
                arrayOf(LocalTime::class),
                arrayOf(LocalDateTime::class),
                arrayOf(DateTime::class),
                arrayOf(Period::class),
                arrayOf(Duration::class),
                arrayOf(DateTimeZone::class),
                arrayOf(Interval::class),
                arrayOf(YearMonth::class),
                arrayOf(MonthDay::class)
            )
        }
    }
}
