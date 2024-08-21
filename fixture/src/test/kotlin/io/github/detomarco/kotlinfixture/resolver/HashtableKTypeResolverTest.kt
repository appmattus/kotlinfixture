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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Dictionary
import java.util.Hashtable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class HashtableKTypeResolverTest {

    class Single {
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

    @RunWith(Parameterized::class)
    class Parameterised {

        val context = TestContext(
            Configuration(),
            CompositeResolver(HashtableKTypeResolver(), StringResolver(), PrimitiveResolver(), KTypeResolver())
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
                context.resolve(type) as Dictionary<*, *>
            }
        }

        @Test
        fun `Uses seeded random`() {
            val value1 = context.seedRandom().resolve(type) as Dictionary<*, *>
            val value2 = context.seedRandom().resolve(type) as Dictionary<*, *>

            assertEquals(value1, value2)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{1}")
            fun data() = arrayOf(
                arrayOf(typeOf<Dictionary<String, String>>(), Dictionary::class),
                arrayOf(typeOf<Hashtable<String, String>>(), Hashtable::class)
            )
        }
    }
}
