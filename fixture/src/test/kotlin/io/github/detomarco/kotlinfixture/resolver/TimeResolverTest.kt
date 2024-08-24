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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
import java.util.Date
import java.util.TimeZone
import kotlin.reflect.KClass

class TimeResolverTest {

    @Nested
    inner class Single {
        private val now = Date()

        private val context = TestContext(
            ConfigurationBuilder().apply { factory<Date> { before(now) } }.build(),
            CompositeResolver(TimeResolver(), FactoryResolver(), KTypeResolver())
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
                    result.toInstant() <= now.toInstant()
                }
            }
        }

        @Test
        fun `Can override ZoneId when creating ZonedDateTime`() {
            repeat(100) {
                val zonedDateTime = kotlinFixture {
                    factory<ZoneId> { ZoneId.of("Europe/London") }
                }

                assertEquals("Europe/London", zonedDateTime<ZonedDateTime>().zone.id)
            }
        }
    }

    private val context = TestContext(
        Configuration(),
        CompositeResolver(TimeResolver(), KTypeResolver(), DateResolver(), EnumResolver())
    )

    @ParameterizedTest
    @MethodSource("data")
    fun `Class returns date`(type: KClass<*>) {
        val result = context.resolve(type)

        assertNotNull(result)
        assertTrue {
            type.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(type: KClass<*>) {
        assertIsRandom {
            context.resolve(type)
        }
    }

    companion object {
        @JvmStatic
        fun data() = arrayOf(
            Arguments.of(ZonedDateTime::class),
            Arguments.of(LocalDate::class),
            Arguments.of(LocalTime::class),
            Arguments.of(LocalDateTime::class),
            Arguments.of(OffsetDateTime::class),
            Arguments.of(OffsetTime::class),
            Arguments.of(Instant::class),
            Arguments.of(Period::class),
            Arguments.of(Duration::class),
            Arguments.of(TimeZone::class),
            Arguments.of(ZoneId::class),
            Arguments.of(ZoneOffset::class),
            Arguments.of(Year::class),
            Arguments.of(Month::class),
            Arguments.of(YearMonth::class),
            Arguments.of(MonthDay::class)

        )
    }
}
