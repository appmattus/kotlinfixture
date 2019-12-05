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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
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

@RunWith(Enclosed::class)
class MapKTypeResolverTest {

    class Single {
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

    @RunWith(Parameterized::class)
    class Parameterised {

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

        @Parameterized.Parameter(0)
        lateinit var type: KType

        @Parameterized.Parameter(1)
        lateinit var resultClass: KClass<*>

        @Test
        fun `creates instance`() {
            val result = context.resolve(type)

            assertTrue {
                resultClass.isInstance(result)
            }
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                (context.resolve(type) as MutableMap<*, *>)
            }
        }

        @Test
        fun `Uses seeded random`() {
            val value1 = (context.seedRandom().resolve(type) as MutableMap<*, *>).toMap()
            val value2 = (context.seedRandom().resolve(type) as MutableMap<*, *>).toMap()

            assertEquals(value1, value2)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{1}")
            fun data() = arrayOf(
                arrayOf(typeOf<Map<String, String>>(), Map::class),
                arrayOf(typeOf<SortedMap<String, String>>(), SortedMap::class),
                arrayOf(typeOf<NavigableMap<String, String>>(), NavigableMap::class),
                arrayOf(typeOf<ConcurrentMap<String, String>>(), ConcurrentMap::class),
                arrayOf(typeOf<ConcurrentNavigableMap<String, String>>(), ConcurrentNavigableMap::class),
                arrayOf(typeOf<java.util.AbstractMap<String, String>>(), java.util.AbstractMap::class),
                arrayOf(typeOf<HashMap<String, String>>(), HashMap::class),
                arrayOf(typeOf<LinkedHashMap<String, String>>(), LinkedHashMap::class),
                arrayOf(typeOf<IdentityHashMap<String, String>>(), IdentityHashMap::class),
                arrayOf(typeOf<WeakHashMap<String, String>>(), WeakHashMap::class),
                arrayOf(typeOf<TreeMap<String, String>>(), TreeMap::class),
                arrayOf(typeOf<ConcurrentHashMap<String, String>>(), ConcurrentHashMap::class),
                arrayOf(typeOf<ConcurrentSkipListMap<String, String>>(), ConcurrentSkipListMap::class)
            )
        }
    }
}
