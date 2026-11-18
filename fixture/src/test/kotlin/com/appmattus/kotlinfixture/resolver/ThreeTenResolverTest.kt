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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.Month
import org.threeten.bp.MonthDay
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.Period
import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.util.Date
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ThreeTenResolverTest {

    class Single {
        private val now = Date()

        private val context = TestContext(
            ConfigurationBuilder().apply { factory<Date> { before(now) } }.build(),
            CompositeResolver(ThreeTenResolver(), FactoryResolver(), KTypeResolver())
        )

        private val unconfiguredContext = TestContext(
            Configuration(),
            CompositeResolver(ThreeTenResolver(), KTypeResolver(), DateResolver())
        )

        @Test
        fun `Before specification gives Year and YearMonth no later than now`() {
            // The latest current date anywhere, as values are generated in a random time zone
            val latestNow = LocalDate.now(ZoneOffset.MAX)

            repeat(100) {
                val year = context.resolve(Year::class) as Year
                val yearMonth = context.resolve(YearMonth::class) as YearMonth

                assertTrue("$year is after $latestNow") { year.value <= latestNow.year }
                assertTrue("$yearMonth is after $latestNow") { yearMonth <= YearMonth.from(latestNow) }
            }
        }

        @Test
        fun `Year and YearMonth are realistic`() {
            repeat(100) {
                val year = unconfiguredContext.resolve(Year::class) as Year
                val yearMonth = unconfiguredContext.resolve(YearMonth::class) as YearMonth

                assertTrue("$year is not within ten years of 2020") { year.value in 2009..2030 }
                assertTrue("$yearMonth is not within ten years of 2020") { yearMonth.year in 2009..2030 }
            }
        }

        @Test
        fun `MonthDay generates every valid day of every month`() {
            val expected = Month.values().flatMap { month -> (1..month.maxLength()).map { MonthDay.of(month, it) } }

            val generated = List(MONTH_DAY_SAMPLES) { unconfiguredContext.resolve(MonthDay::class) as MonthDay }.toSet()

            assertEquals(366, expected.size)
            assertEquals(expected.toSet() - generated, emptySet(), "Valid days never generated")
        }

        private companion object {
            private const val SEED = 7

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
                val result = context.resolve(ZonedDateTime::class) as ZonedDateTime

                assertTrue {
                    result.toInstant() <= DateTimeUtils.toInstant(now)
                }
            }
        }

        @Test
        fun `Can override ZoneId when creating ZonedDateTime`() {
            repeat(100) {
                val fixture = kotlinFixture {
                    factory<ZoneId> { ZoneId.of("Europe/London") }
                }
                val zonedDateTime = fixture<ZonedDateTime>()

                assertEquals("Europe/London", zonedDateTime.zone.id)
            }
        }

        @Test
        fun `seeded ZoneId is picked from the zones in sorted order`() {
            val fixture = kotlinFixture { random = Random(SEED) }

            assertEquals(ZoneId.of(ZoneId.getAvailableZoneIds().sorted().random(Random(SEED))), fixture<ZoneId>())
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        @Parameterized.Parameter(0)
        lateinit var type: KClass<*>

        @Suppress("UNCHECKED_CAST")
        private val context = TestContext(
            Configuration(),
            CompositeResolver(ThreeTenResolver(), KTypeResolver(), DateResolver(), EnumResolver())
        )

        @Test
        fun `Class returns date`() {
            val result = context.resolve(type)

            assertNotNull(result)
            assertTrue {
                type.isInstance(result)
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
                arrayOf(ZonedDateTime::class),
                arrayOf(LocalDate::class),
                arrayOf(LocalTime::class),
                arrayOf(LocalDateTime::class),
                arrayOf(OffsetDateTime::class),
                arrayOf(OffsetTime::class),
                arrayOf(Instant::class),
                arrayOf(Period::class),
                arrayOf(Duration::class),
                arrayOf(ZoneId::class),
                arrayOf(ZoneOffset::class),
                arrayOf(Year::class),
                arrayOf(Month::class),
                arrayOf(YearMonth::class),
                arrayOf(MonthDay::class)
            )
        }
    }
}
