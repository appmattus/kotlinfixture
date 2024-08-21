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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CharResolverTest {
    private val context = TestContext(Configuration(), CharResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Char class returns char`() {
        val result = context.resolve(Char::class)

        assertNotNull(result)
        assertEquals(Char::class, result::class)
    }

    @Test
    fun `Char class returns lowercase char`() {
        repeat(100) {
            val result = context.resolve(Char::class)

            assertTrue {
                result in 'a'..'z'
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Char::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(Char::class) as Char
        val value2 = context.seedRandom().resolve(Char::class) as Char

        assertEquals(value1, value2)
    }
}
