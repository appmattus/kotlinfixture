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

@RunWith(Enclosed::class)
class PrimitiveResolverTest {

    class Single {
        val context = TestContext(Configuration(), PrimitiveResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {
        val context = TestContext(Configuration(), PrimitiveResolver())

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

        @Test
        fun `Uses seeded random`() {
            val value1 = context.seedRandom().resolve(clazz)
            val value2 = context.seedRandom().resolve(clazz)

            assertEquals(value1, value2)
        }

        companion object {
            @JvmStatic
            @Suppress("EXPERIMENTAL_API_USAGE")
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(Boolean::class),
                arrayOf(Byte::class),
                arrayOf(Double::class),
                arrayOf(Float::class),
                arrayOf(Int::class),
                arrayOf(Long::class),
                arrayOf(Short::class),

                arrayOf(UByte::class),
                arrayOf(UInt::class),
                arrayOf(ULong::class),
                arrayOf(UShort::class)
            )
        }
    }
}
