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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class PrimitiveArrayResolverTest {

    class Single {
        val context = TestContext(Configuration(), CompositeResolver(PrimitiveArrayResolver(), PrimitiveResolver()))

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }

        @Test
        fun `Length of array matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            val result = context.resolve(IntArray::class) as IntArray

            assertEquals(3, result.size)
        }

        @Test
        fun `Length of array matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            val result = context.resolve(IntArray::class) as IntArray

            assertEquals(7, result.size)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {
        val context = TestContext(
            Configuration(),
            CompositeResolver(PrimitiveArrayResolver(), PrimitiveResolver(), CharResolver())
        )

        @Parameterized.Parameter(0)
        lateinit var clazz: KClass<*>

        @Test
        fun `Returns correct type`() {
            val result = context.resolve(clazz)

            assertNotNull(result)
            assertEquals(clazz, result::class)
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                context.resolve(clazz)
            }
        }

        companion object {
            @OptIn(ExperimentalUnsignedTypes::class)
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(BooleanArray::class),
                arrayOf(ByteArray::class),
                arrayOf(DoubleArray::class),
                arrayOf(FloatArray::class),
                arrayOf(IntArray::class),
                arrayOf(LongArray::class),
                arrayOf(ShortArray::class),
                arrayOf(CharArray::class),

                arrayOf(UByteArray::class),
                arrayOf(UIntArray::class),
                arrayOf(ULongArray::class),
                arrayOf(UShortArray::class)
            )
        }
    }
}
