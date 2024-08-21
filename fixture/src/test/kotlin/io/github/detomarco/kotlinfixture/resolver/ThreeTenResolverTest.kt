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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.config.Configuration
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.config.before
import io.github.detomarco.kotlinfixture.kotlinFixture
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
                val zonedDateTime = kotlinFixture {
                    factory<ZoneId> { ZoneId.of("Europe/London") }
                } <ZonedDateTime>()

                assertEquals("Europe/London", zonedDateTime.zone.id)
            }
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
