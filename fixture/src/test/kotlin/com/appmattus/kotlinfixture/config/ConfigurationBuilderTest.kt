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

package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.resolver.StringResolver
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConfigurationBuilderTest {

    @Test
    fun `can override repeatCount`() {
        val configuration = ConfigurationBuilder(Configuration(repeatCount = { 5 })).apply {
            repeatCount { 3 }
        }.build()

        assertEquals(3, configuration.repeatCount())
    }

    @Test
    fun `can override random`() {
        val newRandom = Random(10)
        val configuration = ConfigurationBuilder(Configuration(random = Random(1))).apply {
            random = newRandom
        }.build()

        assertEquals(newRandom, configuration.random)
    }

    data class Properties(val property: String)

    @Test
    fun `can override properties using propertyOf(String)`() {
        val configuration = ConfigurationBuilder(
            Configuration(properties = mapOf(Properties::class to mapOf("property" to { 2 })))
        ).apply {
            property<Properties>("property") { 1 }
        }.build()

        assertEquals(1, (configuration.properties[Properties::class]?.get("property"))?.invoke())
    }

    @Test
    fun `can override properties using propertyOf(KClass, String)`() {
        val configuration = ConfigurationBuilder(
            Configuration(properties = mapOf(Properties::class to mapOf("property" to { 2 })))
        ).apply {
            property(Properties::class, "property") { 1 }
        }.build()

        assertEquals(1, (configuration.properties[Properties::class]?.get("property"))?.invoke())
    }

    @Test
    fun `can override properties using property(KProperty)`() {
        val configuration = ConfigurationBuilder(
            Configuration(properties = mapOf(Properties::class to mapOf("property" to { 2 })))
        ).apply {
            property(Properties::property) { 1 }
        }.build()

        assertEquals(1, configuration.properties[Properties::class]?.get("property")?.invoke())
    }

    @Test
    fun `properties is immutable`() {
        val configuration = ConfigurationBuilder(Configuration()).build()

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST")
            (configuration.properties as MutableMap<KClass<*>, Map<String, () -> Any?>>)[Properties::class] =
                mapOf("property" to { 1 })
        }
    }

    @Test
    fun `can override instances using instance()`() {
        val original: Generator<Properties>.() -> Properties = { Properties("2") }

        @Suppress("UNCHECKED_CAST")
        val configuration = ConfigurationBuilder(
            Configuration(
                factories = mapOf(Properties::class.starProjectedType to original as Generator<Any?>.() -> Any?)
            )
        ).apply {
            factory<Properties> { Properties("1") }
        }.build()

        with(TestGenerator) {
            assertEquals(
                Properties("1"),
                (configuration.factories.getValue(Properties::class.starProjectedType))()
            )
        }
    }

    @Test
    fun `can override instances using instance(KType)`() {
        val original: Generator<Properties>.() -> Properties = { Properties("2") }

        @Suppress("UNCHECKED_CAST")
        val configuration = ConfigurationBuilder(
            Configuration(
                factories = mapOf(Properties::class.starProjectedType to original as Generator<Any?>.() -> Any?)
            )
        ).apply {
            factory(Properties::class.starProjectedType) { Properties("1") }
        }.build()

        with(TestGenerator) {
            assertEquals(
                Properties("1"),
                (configuration.factories.getValue(Properties::class.starProjectedType))()
            )
        }
    }

    @Test
    fun `instances is immutable`() {
        val configuration = ConfigurationBuilder(Configuration()).build()

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST", "ReplacePutWithAssignment")
            (configuration.factories as MutableMap<KType, () -> Any?>).put(Properties::class.starProjectedType) {
                Properties("1")
            }
        }
    }

    @Test
    fun `can override subType`() {
        val configuration = ConfigurationBuilder(
            Configuration(subTypes = mapOf(Number::class to Int::class))
        ).apply {
            subType<Number, Float>()
        }.build()

        assertEquals(Float::class, configuration.subTypes[Number::class])
    }

    @Test
    fun `can override subType(KClass, KClass)`() {
        val configuration = ConfigurationBuilder(
            Configuration(subTypes = mapOf(Number::class to Int::class))
        ).apply {
            subType(Number::class, Float::class)
        }.build()

        assertEquals(Float::class, configuration.subTypes[Number::class])
    }

    @Test
    fun `subTypes is immutable`() {
        val configuration = ConfigurationBuilder(Configuration()).build()

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST", "ReplacePutWithAssignment")
            (configuration.subTypes as MutableMap<KClass<*>, KClass<*>>).put(Number::class, Double::class)
        }
    }

    private val testDecorator = object : Decorator {
        override fun decorate(resolver: Resolver) = resolver
    }

    @Test
    fun `can override decorators using add`() {
        val configuration = ConfigurationBuilder(
            Configuration(decorators = listOf(testDecorator))
        ).apply {
            decorators.add(testDecorator)
        }.build()

        assertEquals(2, configuration.decorators.size)
    }

    @Test
    fun `can override decorators by replacement`() {
        val configuration = ConfigurationBuilder(
            Configuration(decorators = listOf(testDecorator, testDecorator))
        ).apply {
            decorators = mutableListOf(testDecorator)
        }.build()

        assertEquals(1, configuration.decorators.size)
    }

    @Test
    fun `decorators is immutable`() {
        val configuration = ConfigurationBuilder(Configuration()).build()

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST")
            (configuration.decorators as MutableCollection<Decorator>).add(testDecorator)
        }
    }

    private val testResolver = StringResolver()

    @Test
    fun `can override resolvers using add`() {
        val configuration = ConfigurationBuilder(
            Configuration(resolvers = listOf(testResolver))
        ).apply {
            resolvers.add(testResolver)
        }.build()

        assertEquals(2, configuration.resolvers.size)
    }

    @Test
    fun `can override resolvers by replacement`() {
        val configuration = ConfigurationBuilder(
            Configuration(resolvers = listOf(testResolver, testResolver))
        ).apply {
            resolvers = mutableListOf(testResolver)
        }.build()

        assertEquals(1, configuration.resolvers.size)
    }

    @Test
    fun `resolvers is immutable`() {
        val configuration = ConfigurationBuilder(Configuration()).build()

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST")
            (configuration.resolvers as MutableCollection<Resolver>).add(testResolver)
        }
    }
}
