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
import java.util.Currency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CurrencyResolverTest {

    private val context = TestContext(Configuration(), CurrencyResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Currency class returns currency`() {
        val result = context.resolve(Currency::class)

        assertNotNull(result)
        assertEquals(Currency::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Currency::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(Currency::class) as Currency
        val value2 = context.seedRandom().resolve(Currency::class) as Currency

        assertEquals(value1, value2)
    }
}
