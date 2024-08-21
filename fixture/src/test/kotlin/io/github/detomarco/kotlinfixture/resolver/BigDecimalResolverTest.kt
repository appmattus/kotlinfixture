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
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BigDecimalResolverTest {
    private val context = TestContext(Configuration(), BigDecimalResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `BigDecimal class returns big decimal`() {
        val result = context.resolve(BigDecimal::class)

        assertNotNull(result)
        assertEquals(BigDecimal::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(BigDecimal::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(BigDecimal::class) as BigDecimal
        val value2 = context.seedRandom().resolve(BigDecimal::class) as BigDecimal

        assertEquals(value1, value2)
    }
}
