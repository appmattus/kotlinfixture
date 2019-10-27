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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TupleKTypeResolverTest {
    val context = TestContext(
        Configuration(),
        CompositeResolver(TupleKTypeResolver(), StringResolver(), KTypeResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Unknown Pair key type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<Pair<Number, String>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Unknown Pair value type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<Pair<String, Number>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Pair random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<Pair<String, String>?>()) == null
        }
    }

    @Test
    fun `Pair creates instance`() {
        val result = context.resolve(typeOf<Pair<String, String>>())

        assertTrue {
            Pair::class.isInstance(result)
        }
    }

    @Test
    fun `Pair random values returned`() {
        assertIsRandom {
            context.resolve(typeOf<Pair<String, String>>())
        }
    }

    @Test
    fun `Pair uses seeded random`() {
        val value1 = context.seedRandom().resolve(typeOf<Pair<String, String>>())
        val value2 = context.seedRandom().resolve(typeOf<Pair<String, String>>())

        assertEquals(value1, value2)
    }

    @Test
    fun `Unknown Triple first type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<Triple<Number, String, String>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Unknown Triple second type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<Triple<String, Number, String>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Unknown Triple third type parameter returns Unresolved`() {
        val result = context.resolve(typeOf<Triple<String, String, Number>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Triple random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<Triple<String, String, String>?>()) == null
        }
    }

    @Test
    fun `Triple creates instance`() {
        val result = context.resolve(typeOf<Triple<String, String, String>>())

        assertTrue {
            Triple::class.isInstance(result)
        }
    }

    @Test
    fun `Triple random values returned`() {
        assertIsRandom {
            context.resolve(typeOf<Triple<String, String, String>>())
        }
    }

    @Test
    fun `Triple uses seeded random`() {
        val value1 = context.seedRandom().resolve(typeOf<Triple<String, String, String>>())
        val value2 = context.seedRandom().resolve(typeOf<Triple<String, String, String>>())

        assertEquals(value1, value2)
    }
}
