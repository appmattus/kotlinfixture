/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.TestContext
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.assertIsRandom
import com.detomarco.kotlinfixture.config.Configuration
import com.detomarco.kotlinfixture.typeOf
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicIntegerArray
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicLongArray
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class AtomicKTypeResolverTest {

    class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(AtomicKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Unknown type parameter returns Unresolved`() {
            val result = context.resolve(typeOf<AtomicReference<Number>>())

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Random nullability returned`() {
            assertIsRandom {
                context.resolve(typeOf<AtomicReference<String>?>()) == null
            }
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        private val context = TestContext(
            Configuration(),
            CompositeResolver(
                AtomicKTypeResolver(),
                StringResolver(),
                PrimitiveResolver(),
                PrimitiveArrayResolver(),
                KTypeResolver()
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
                context.resolve(type).toString()
            }
        }

        @Test
        fun `Uses seeded random`() {
            val value1 = context.seedRandom().resolve(type)
            val value2 = context.seedRandom().resolve(type)

            assertEquals(value1.toString(), value2.toString())
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{1}")
            fun data() = arrayOf(
                arrayOf(typeOf<AtomicBoolean>(), AtomicBoolean::class),
                arrayOf(typeOf<AtomicInteger>(), AtomicInteger::class),
                arrayOf(typeOf<AtomicLong>(), AtomicLong::class),
                arrayOf(typeOf<AtomicIntegerArray>(), AtomicIntegerArray::class),
                arrayOf(typeOf<AtomicLongArray>(), AtomicLongArray::class),
                arrayOf(typeOf<AtomicReference<String>>(), AtomicReference::class)
            )
        }
    }
}
