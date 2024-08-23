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
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Period
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Date
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JodaTimeResolverTest {

    @Nested
    inner class Single {
        private val now = Date()

        private val context = TestContext(
            ConfigurationBuilder().apply { factory<Date> { before(now) } }.build(),
            CompositeResolver(JodaTimeResolver(), FactoryResolver(), KTypeResolver())
        )

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
                val dateTime = kotlinFixture {
                    factory<DateTimeZone> { DateTimeZone.forID("Europe/London") }
                }

                assertEquals("Europe/London", dateTime<DateTime>().zone.id)
            }
        }
    }

    private val context = TestContext(
        Configuration(),
        CompositeResolver(JodaTimeResolver(), KTypeResolver(), DateResolver(), EnumResolver())
    )

    @ParameterizedTest
    @MethodSource("data")
    fun `Class returns date`(type: KClass<*>) {
        repeat(100) {
            val result = context.resolve(type)

            assertNotNull(result)
            assertTrue {
                type.isInstance(result)
            }
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
            Arguments.of(Instant::class),
            Arguments.of(LocalDate::class),
            Arguments.of(LocalTime::class),
            Arguments.of(LocalDateTime::class),
            Arguments.of(DateTime::class),
            Arguments.of(Period::class),
            Arguments.of(Duration::class),
            Arguments.of(DateTimeZone::class),
            Arguments.of(Interval::class)
        )
    }
}
