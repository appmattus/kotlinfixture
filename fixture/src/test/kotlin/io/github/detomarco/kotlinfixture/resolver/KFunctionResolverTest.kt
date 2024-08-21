/*
 * Copyright 2020 Appmattus Limited
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
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KFunctionResolverTest {
    private val context = TestContext(
        Configuration(),
        CompositeResolver(
            PrimitiveResolver(),
            StringResolver(),
            KTypeResolver(),
            KNamedPropertyResolver(),
            KFunctionResolver()
        )
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Constructor creates instance`() {
        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = context.resolve(request)

        assertNotNull(result)
        assertEquals(SimpleClass::class, result::class)
    }

    @Test
    fun `Calls function on object`() {
        val request = KFunctionRequest(SimpleObject::class, SimpleObject::set)

        assertFalse(SimpleObject.isInitialised)

        context.resolve(request)

        assertTrue(SimpleObject.isInitialised)
    }

    @Test
    fun `Random values returned`() {
        val request = KFunctionRequest(SimpleClass::class, SimpleClass::class.primaryConstructor!!)

        assertIsRandom {
            val result = context.resolve(request) as SimpleClass
            result.value
        }
    }

    @Test
    fun `Uses seeded random`() {
        val request = KFunctionRequest(SimpleClass::class, SimpleClass::class.primaryConstructor!!)

        val value1 = context.seedRandom().resolve(request) as SimpleClass
        val value2 = context.seedRandom().resolve(request) as SimpleClass

        assertEquals(value1, value2)
    }

    @Test
    fun `Constructor creates instance with provided parameter`() {
        val context = context.copy(
            configuration = ConfigurationBuilder().apply {
                property(SimpleClass::value) { "custom" }
            }.build()
        )

        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = context.resolve(request) as SimpleClass

        assertEquals("custom", result.value)
    }

    @Test
    fun `Constructor with unresolvable parameter fails`() {

        val constructor = UnresolvableClass::class.primaryConstructor!!
        val request = KFunctionRequest(UnresolvableClass::class, constructor)

        val result = context.resolve(request)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Constructor with nullable parameter is sometimes null`() {
        val constructor = NullableClass::class.primaryConstructor!!
        val request = KFunctionRequest(NullableClass::class, constructor)

        assertIsRandom {
            val result = context.resolve(request) as NullableClass
            result.value == null
        }
    }

    @Test
    fun `Constructor with multiple parameters returns`() {
        val constructor = MultiParamsClass::class.primaryConstructor!!
        val request = KFunctionRequest(MultiParamsClass::class, constructor)

        val result = context.resolve(request)

        assertNotNull(result)
        assertEquals(MultiParamsClass::class, result::class)
    }

    @Test
    fun `Can create class with no params factory method`() {
        val constructor = FactoryClass.Companion::class.functions.first { it.name == "noParams" }
        val request = KFunctionRequest(FactoryClass::class, constructor)

        val result = context.resolve(request) as FactoryClass
        assertEquals("default", result.value)
    }

    @Test
    fun `Can create class with random value using params factory method`() {
        val constructor = FactoryClass.Companion::class.functions.first { it.name == "oneParam" }
        val request = KFunctionRequest(FactoryClass::class, constructor)

        assertIsRandom {
            (context.resolve(request) as FactoryClass).value
        }
    }

    data class NullableClass(val value: String?)

    data class UnresolvableClass(val value: Number)

    data class SimpleClass(val value: String)

    data class MultiParamsClass(val value1: String, val value2: Int)

    class FactoryClass private constructor(val value: String) {

        companion object {
            @Suppress("unused")
            fun noParams() = FactoryClass("default")

            @Suppress("unused")
            fun oneParam(value: String) = FactoryClass(value)
        }
    }

    object SimpleObject {
        @Suppress("MemberVisibilityCanBePrivate")
        lateinit var value: String

        val isInitialised: Boolean
            get() = ::value.isInitialized

        fun set(value: String) {
            this.value = value
        }
    }
}
