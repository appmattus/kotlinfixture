/*
 * Copyright 2020-2023 Appmattus Limited
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ArrayKTypeResolverTest {
    private val context = TestContext(
        Configuration(),
        CompositeResolver(ArrayKTypeResolver(), StringResolver(), PrimitiveResolver(), KTypeResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Primitive array class returns Unresolved`() {
        val result = context.resolve(IntArray::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Array-String class returns string array`() {
        val result = context.resolve(typeOf<Array<String>>())

        assertNotNull(result)
        assertEquals(Array<String>::class, result::class)
    }

    @Test
    fun `Array-NullableString class returns nullable string array`() {
        assertIsRandom {
            val result = context.resolve(typeOf<Array<String?>>())

            assertNotNull(result)
            assertEquals(Array<String?>::class, result::class)

            @Suppress("UNCHECKED_CAST")
            (result as Array<String?>)[0] == null
        }
    }

    @Test
    fun `BooleanArray class returns unresolved`() {
        val result = context.resolve(typeOf<BooleanArray>())

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(typeOf<Array<String>>())
        }
    }

    @Test
    fun `Length of array matches configuration value of 3`() {
        val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

        val result = context.resolve(typeOf<Array<String>>()) as Array<*>

        assertEquals(3, result.size)
    }

    @Test
    fun `Length of array matches configuration value of 7`() {
        val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

        val result = context.resolve(typeOf<Array<String>>()) as Array<*>

        assertEquals(7, result.size)
    }

    @Test
    fun `Array of arrays`() {
        val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

        @Suppress("UNCHECKED_CAST")
        val result = context.resolve(typeOf<Array<Array<String>>>()) as Array<Array<String>>

        assertEquals(3, result.size)
        result.forEach {
            assertEquals(3, it.size)
        }
    }
}
