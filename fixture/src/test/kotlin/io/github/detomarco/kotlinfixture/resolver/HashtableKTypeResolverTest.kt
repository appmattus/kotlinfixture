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
import io.github.detomarco.kotlinfixture.typeOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Dictionary
import java.util.Hashtable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HashtableKTypeResolverTest {

    @Nested
    inner class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(HashtableKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown key type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<Hashtable<Number, String>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown value type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<Hashtable<String, Number>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Random nullability returned`() {
            assertIsRandom {
                context.resolve(typeOf<Hashtable<String, String>?>()) == null
            }
        }

        @Test
        fun `Length matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Hashtable<String, String>>()) as Hashtable<String, String>

            assertEquals(3, result.size)
        }

        @Test
        fun `Length matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Hashtable<String, String>>()) as Hashtable<String, String>

            assertEquals(7, result.size)
        }
    }

    private val contextParam = TestContext(
        Configuration(),
        CompositeResolver(HashtableKTypeResolver(), StringResolver(), PrimitiveResolver(), KTypeResolver())
    )

    @ParameterizedTest
    @MethodSource("data")
    fun `creates instance`(type: KType, resultClass: KClass<*>) {
        val result = contextParam.resolve(type)

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(type: KType) {
        assertIsRandom {
            contextParam.resolve(type) as Dictionary<*, *>
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Uses seeded random`(type: KType) {
        val value1 = contextParam.seedRandom().resolve(type) as Dictionary<*, *>
        val value2 = contextParam.seedRandom().resolve(type) as Dictionary<*, *>

        assertEquals(value1, value2)
    }

    companion object {
        @JvmStatic
        fun data() = arrayOf(
            Arguments.of(typeOf<Dictionary<String, String>>(), Dictionary::class),
            Arguments.of(typeOf<Hashtable<String, String>>(), Hashtable::class)
        )
    }
}
