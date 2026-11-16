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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.before
import com.appmattus.kotlinfixture.kotlinFixture
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toJavaYearMonth
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class KotlinxDateTimeResolverTest {

    class Single {
        private val context = TestContext(Configuration(), KotlinxDateTimeResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `YearMonth has a realistic year`() {
            val fixture = kotlinFixture()

            repeat(100) {
                val result = fixture<YearMonth>()

                assertTrue("$result is not within ten years of 2020") { result.year in 2009..2030 }
            }
        }

        @Test
        fun `Before specification gives YearMonth no later than now`() {
            val now = Date()
            val fixture = kotlinFixture {
                factory<Date> { before(now) }
            }
            // The latest current date anywhere, as values are generated in a random time zone
            val latestNow = java.time.YearMonth.now(ZoneOffset.MAX)

            repeat(100) {
                val result = fixture<YearMonth>()

                assertTrue("$result is after $latestNow") {
                    result.toJavaYearMonth() <= latestNow
                }
            }
        }

        @Test
        fun `periods have realistic lengths`() {
            val fixture = kotlinFixture()

            repeat(100) {
                assertTrue { fixture<DatePeriod>().years in -100..100 }
                assertTrue { fixture<DateTimePeriod>().years in -100..100 }
            }
        }

        @Test
        fun `FixedOffsetTimeZone is a fixed offset`() {
            val fixture = kotlinFixture()

            repeat(100) {
                val timeZone = fixture<FixedOffsetTimeZone>()

                assertEquals(timeZone.offset.toString(), timeZone.id)
            }
        }

        @Test
        fun `TimeZone uses configured ZoneId`() {
            val fixture = kotlinFixture {
                factory<ZoneId> { ZoneId.of("Europe/London") }
            }

            assertEquals(TimeZone.of("Europe/London"), fixture<TimeZone>())
        }

        @Test
        fun `Generated as a property of a class`() {
            data class Event(val date: LocalDate, val time: LocalTime, val zone: TimeZone)

            val event = kotlinFixture()<Event>()

            assertNotNull(event.date)
            assertNotNull(event.time)
            assertNotNull(event.zone)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        @Parameterized.Parameter(0)
        lateinit var type: KClass<*>

        private val context = TestContext(
            Configuration(),
            CompositeResolver(KotlinxDateTimeResolver(), TimeResolver(), KTypeResolver(), DateResolver())
        )

        @Test
        fun `Class returns instance`() {
            repeat(100) {
                val result = context.resolve(type)

                assertNotNull(result)
                assertTrue("$result is not a $type") {
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

        @Test
        fun `Uses seeded random`() {
            val seeded = { context.copy(configuration = context.configuration.copy(random = Random(0))) }

            assertEquals(seeded().resolve(type), seeded().resolve(type))
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(LocalDate::class),
                arrayOf(LocalDateTime::class),
                arrayOf(LocalTime::class),
                arrayOf(YearMonth::class),
                arrayOf(TimeZone::class),
                arrayOf(FixedOffsetTimeZone::class),
                arrayOf(UtcOffset::class),
                arrayOf(DatePeriod::class),
                arrayOf(DateTimePeriod::class)
            )
        }
    }
}
