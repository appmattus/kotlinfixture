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
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.config.before
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CalendarResolverTest {
    private val now = Date()

    private val context = TestContext(
        ConfigurationBuilder().apply { factory<Date> { before(now) } }.build(),
        CompositeResolver(CalendarResolver(), FactoryResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Calendar class returns date`() {
        val result = context.resolve(Calendar::class)

        assertNotNull(result)
        assertTrue {
            result is Calendar
        }
    }

    @Test
    fun `Before specification gives date in the past`() {
        repeat(100) {
            val result = context.resolve(Calendar::class) as Calendar

            assertTrue {
                result.time.time <= now.time
            }
        }
    }

    @Test
    fun `Random values returned for Calendar`() {
        assertIsRandom {
            context.resolve(Calendar::class)
        }
    }

    @Test
    fun `GregorianCalendar class returns date`() {
        val result = context.resolve(GregorianCalendar::class)

        assertNotNull(result)
        assertTrue {
            result is GregorianCalendar
        }
    }

    @Test
    fun `Random values returned for GregorianCalendar`() {
        assertIsRandom {
            context.resolve(GregorianCalendar::class)
        }
    }
}
