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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType

class KTypeResolverTest {
    private val context = TestContext(Configuration(), CompositeResolver(PrimitiveResolver(), KTypeResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Int kType calls resolver with Int`() {
        val result = context.resolve(Int::class.createType())

        assertNotNull(result)
        assertEquals(Int::class, result!!::class)
    }

    @Test
    fun `Nullable Int kType randomly returns either null or Int`() {
        assertIsRandom {
            context.resolve(Int::class.createType(nullable = true)) == null
        }
    }

    @Test
    fun `Uses seeded random`() {
        val context1 = context.seedRandom()
        val value1 = List(5) {
            context1.resolve(Int::class.createType(nullable = true))
        }

        val context2 = context.seedRandom()
        val value2 = List(5) {
            context2.resolve(Int::class.createType(nullable = true))
        }

        assertEquals(value1, value2)
    }
}
