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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(Enclosed::class)
class DateResolverTest {

    class Single {

        private val context = TestContext(Configuration(), DateResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        @Parameterized.Parameter(0)
        lateinit var type: KClass<*>

        private val context = TestContext(Configuration(), DateResolver())

        @Test
        fun `Class returns date`() {
            val result = context.resolve(type)

            assertNotNull(result)
            assertEquals(type, result::class)
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                context.resolve(type)
            }
        }

        @Test
        fun `Uses seeded random`() {
            val value1 = context.seedRandom().resolve(type) as Date
            val value2 = context.seedRandom().resolve(type) as Date

            assertEquals(value1.time, value2.time)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(Date::class),
                arrayOf(java.sql.Date::class),
                arrayOf(java.sql.Time::class),
                arrayOf(java.sql.Timestamp::class)
            )
        }
    }
}
