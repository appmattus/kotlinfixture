/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.TestContext
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.assertIsRandom
import com.detomarco.kotlinfixture.config.Configuration
import com.detomarco.kotlinfixture.typeOf
import java.util.EnumMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnumMapResolverTest {

    enum class SingleEnumClass {
        @Suppress("unused")
        A
    }

    val context = TestContext(
        Configuration(),
        CompositeResolver(EnumMapResolver(), EnumResolver(), StringResolver(), KTypeResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Unknown value type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<EnumMap<SingleEnumClass, Number>>())

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Length is random`() {
        assertIsRandom {
            @Suppress("UNCHECKED_CAST")
            (context.resolve(typeOf<EnumMap<SingleEnumClass, String>>()) as EnumMap<SingleEnumClass, String>).size
        }
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns empty EnumMap`() {
        val result = context.resolve(typeOf<EnumMap<EmptyEnumClass, String>>())

        assertEquals(EnumMap<EmptyEnumClass, String>(EmptyEnumClass::class.java), result)
    }

    enum class MultiEnumClass {
        @Suppress("unused")
        ValueA,
        @Suppress("unused")
        ValueB
    }

    @Test
    fun `Enum with multiple values returns random value`() {
        assertIsRandom {
            context.resolve(typeOf<EnumMap<MultiEnumClass, String>>())
        }
    }

    @Test
    fun `Random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<EnumMap<MultiEnumClass, String>?>()) == null
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>
        val value2 = context.seedRandom().resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>

        assertEquals(value1, value2)
    }

    @Test
    fun `Enum with multiple values is randomly empty`() {
        assertIsRandom {
            (context.resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>).isEmpty()
        }
    }
}
