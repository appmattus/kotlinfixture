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

package io.github.detomarco.kotlinfixture.config

import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.kotlinFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GeneratorDateTest {

    private val now = Date()

    @Test
    fun `Date class returns date`() {
        val fixture = kotlinFixture()

        val result = fixture<Date>()

        assertNotNull(result)
        assertEquals(Date::class, result::class)
    }

    @Test
    fun `After specification gives date in the future`() {
        val fixture = kotlinFixture {
            factory<Date> { after(now) }
        }

        repeat(100) {
            val result = fixture<Date>()

            assertTrue {
                result.time >= now.time
            }
        }
    }

    @Test
    fun `After specification is random`() {
        val fixture = kotlinFixture {
            factory<Date> { after(now) }
        }

        assertIsRandom {
            fixture<Date>()
        }
    }

    @Test
    fun `After specification uses seeded random`() {
        val fixture = kotlinFixture {
            factory<Date> { after(now) }
        }

        val value1 = fixture<Date> {
            random = Random(0)
        }
        val value2 = fixture<Date> {
            random = Random(0)
        }

        assertEquals(value1, value2)
    }

    @Test
    fun `Before specification gives date in the past`() {
        val fixture = kotlinFixture {
            factory<Date> { before(now) }
        }

        repeat(100) {
            val result = fixture<Date>()

            assertTrue {
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Before specification is random`() {
        val fixture = kotlinFixture {
            factory<Date> { before(now) }
        }

        assertIsRandom {
            fixture<Date>()
        }
    }

    @Test
    fun `Before specification uses seeded random`() {
        val fixture = kotlinFixture {
            factory<Date> { before(now) }
        }

        val value1 = fixture<Date> {
            random = Random(0)
        }
        val value2 = fixture<Date> {
            random = Random(0)
        }

        assertEquals(value1, value2)
    }

    @Test
    fun `Between specification gives date between two dates`() {
        val minTime = now.time - TimeUnit.HOURS.toMillis(1)

        val fixture = kotlinFixture {
            factory<Date> { between(Date(minTime), now) }
        }

        repeat(100) {
            val result = fixture<Date>()

            assertTrue {
                result.time >= minTime
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Between specification is random`() {
        val minTime = now.time - TimeUnit.HOURS.toMillis(1)

        val fixture = kotlinFixture {
            factory<Date> { between(Date(minTime), now) }
        }

        assertIsRandom {
            fixture<Date>()
        }
    }

    @Test
    fun `Between specification uses seeded random`() {
        val minTime = now.time - TimeUnit.HOURS.toMillis(1)

        val fixture = kotlinFixture {
            factory<Date> { between(Date(minTime), now) }
        }

        val value1 = fixture<Date> {
            random = Random(0)
        }
        val value2 = fixture<Date> {
            random = Random(0)
        }

        assertEquals(value1, value2)
    }
}
