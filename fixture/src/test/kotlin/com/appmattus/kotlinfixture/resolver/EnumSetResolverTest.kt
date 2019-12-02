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
import com.appmattus.kotlinfixture.typeOf
import java.util.EnumSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnumSetResolverTest {
    private val context = TestContext(Configuration(), CompositeResolver(EnumSetResolver(), EnumResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns empty EnumSet`() {
        val result = context.resolve(typeOf<EnumSet<EmptyEnumClass>>())

        assertEquals(EnumSet.noneOf(EmptyEnumClass::class.java), result)
    }

    enum class SingleEnumClass {
        @Suppress("unused")
        OnlyValue
    }

    @Test
    fun `Enum with one value returns either empty set or set with one value`() {
        assertIsRandom {
            context.resolve(typeOf<EnumSet<SingleEnumClass>>())
        }
    }

    @Test
    fun `randomly returns null for nullable type`() {
        assertIsRandom {
            context.resolve(typeOf<EnumSet<SingleEnumClass>?>()) == null
        }
    }

    @Test
    fun `Enum with one value returns random length set`() {
        assertIsRandom {
            (context.resolve(typeOf<EnumSet<SingleEnumClass>>()) as EnumSet<*>).size
        }
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
            context.resolve(typeOf<EnumSet<MultiEnumClass>>())
        }
    }

    @Test
    fun `Random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<EnumSet<MultiEnumClass>?>()) == null
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(typeOf<EnumSet<MultiEnumClass>>()) as EnumSet<*>
        val value2 = context.seedRandom().resolve(typeOf<EnumSet<MultiEnumClass>>()) as EnumSet<*>

        assertEquals(value1, value2)
    }
}
