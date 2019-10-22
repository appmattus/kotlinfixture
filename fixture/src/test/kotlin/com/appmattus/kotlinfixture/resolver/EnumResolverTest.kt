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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnumResolverTest {
    private val context = TestContext(Configuration(), EnumResolver())

    private val contextWithTestResolver = context.copy(resolver = CompositeResolver(context.resolver, TestResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns Unresolved`() {
        val result = context.resolve(EmptyEnumClass::class)

        assertEquals(Unresolved, result)
    }

    enum class SingleEnumClass {
        OnlyValue
    }

    @Test
    fun `Enum with one value returns OnlyValue`() {
        val result = contextWithTestResolver.resolve(SingleEnumClass::class)

        assertEquals(SingleEnumClass.OnlyValue, result)
    }

    enum class MultiEnumClass {
        ValueA, ValueB
    }

    @Test
    fun `Enum with multiple values returns random value`() {
        assertIsRandom {
            contextWithTestResolver.resolve(MultiEnumClass::class)
        }
    }

    @Test
    fun `Enum with multiple values returns one of the values`() {
        val result = contextWithTestResolver.resolve(MultiEnumClass::class)

        assertTrue {
            result == MultiEnumClass.ValueA || result == MultiEnumClass.ValueB
        }
    }

    @Test
    fun `Enum order is cached`() {
        assertEquals(
            listOf(
                contextWithTestResolver.resolve(MultiEnumClass::class),
                contextWithTestResolver.resolve(MultiEnumClass::class)
            ),
            listOf(
                contextWithTestResolver.resolve(MultiEnumClass::class),
                contextWithTestResolver.resolve(MultiEnumClass::class)
            )
        )
    }
}
