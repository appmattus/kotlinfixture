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
import org.joda.time.Period
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
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
                } <DateTime>()

                assertEquals("Europe/London", dateTime.zone.id)
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
                arrayOf(Interval::class)
            )
        }
    }
}
