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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.Date
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DateResolverTest {

    @Nested
    inner class Single {

        private val context = TestContext(Configuration(), DateResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }
    }

    private val context = TestContext(Configuration(), DateResolver())

    @ParameterizedTest
    @MethodSource("data")
    fun `Class returns date`(type: KClass<*>) {
        val result = context.resolve(type)

        assertNotNull(result)
        assertEquals(type, result::class)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(type: KClass<*>) {
        assertIsRandom {
            context.resolve(type)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Uses seeded random`(type: KClass<*>) {
        val value1 = context.seedRandom().resolve(type) as Date
        val value2 = context.seedRandom().resolve(type) as Date

        assertEquals(value1.time, value2.time)
    }

    companion object {
        @JvmStatic
        fun data() = arrayOf(
            arrayOf(Date::class),
            arrayOf(java.sql.Date::class),
            arrayOf(java.sql.Time::class),
            arrayOf(java.sql.Timestamp::class)
        )
    }
}
