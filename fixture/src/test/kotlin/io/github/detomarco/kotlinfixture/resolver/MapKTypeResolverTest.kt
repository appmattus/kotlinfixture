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
import java.util.IdentityHashMap
import java.util.NavigableMap
import java.util.SortedMap
import java.util.TreeMap
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapKTypeResolverTest {

    @Nested
    inner class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(MapKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown key type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<Map<Number, String>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown value type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<Map<String, Number>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Random nullability returned`() {
            assertIsRandom {
                context.resolve(typeOf<Map<String, String>?>()) == null
            }
        }

        @Test
        fun `Length matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Map<String, String>>()) as Map<String, String>

            assertEquals(3, result.size)
        }

        @Test
        fun `Length matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            @Suppress("UNCHECKED_CAST")
            val result = context.resolve(typeOf<Map<String, String>>()) as Map<String, String>

            assertEquals(7, result.size)
        }
    }

    private val context = TestContext(
        Configuration(),
        CompositeResolver(
            MapKTypeResolver(),
            StringResolver(),
            PrimitiveResolver(),
            KTypeResolver(),
            KFunctionResolver(),
            ClassResolver()
        )
    )

    @ParameterizedTest
    @MethodSource("data")
    fun `creates instance`(type: KType, resultClass: KClass<*>) {
        val result = context.resolve(type)

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(type: KType) {
        assertIsRandom {
            (context.resolve(type) as MutableMap<*, *>)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Uses seeded random`(type: KType) {
        val value1 = (context.seedRandom().resolve(type) as MutableMap<*, *>).toMap()
        val value2 = (context.seedRandom().resolve(type) as MutableMap<*, *>).toMap()

        assertEquals(value1, value2)
    }

    companion object {
        @JvmStatic
        fun data() = arrayOf(
            Arguments.of(typeOf<Map<String, String>>(), Map::class),
            Arguments.of(typeOf<SortedMap<String, String>>(), SortedMap::class),
            Arguments.of(typeOf<NavigableMap<String, String>>(), NavigableMap::class),
            Arguments.of(typeOf<ConcurrentMap<String, String>>(), ConcurrentMap::class),
            Arguments.of(typeOf<ConcurrentNavigableMap<String, String>>(), ConcurrentNavigableMap::class),
            Arguments.of(typeOf<java.util.AbstractMap<String, String>>(), java.util.AbstractMap::class),
            Arguments.of(typeOf<HashMap<String, String>>(), HashMap::class),
            Arguments.of(typeOf<LinkedHashMap<String, String>>(), LinkedHashMap::class),
            Arguments.of(typeOf<IdentityHashMap<String, String>>(), IdentityHashMap::class),
            Arguments.of(typeOf<WeakHashMap<String, String>>(), WeakHashMap::class),
            Arguments.of(typeOf<TreeMap<String, String>>(), TreeMap::class),
            Arguments.of(typeOf<ConcurrentHashMap<String, String>>(), ConcurrentHashMap::class),
            Arguments.of(typeOf<ConcurrentSkipListMap<String, String>>(), ConcurrentSkipListMap::class)
        )
    }
}
