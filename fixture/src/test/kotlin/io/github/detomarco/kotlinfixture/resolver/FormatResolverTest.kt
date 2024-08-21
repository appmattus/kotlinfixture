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
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class FormatResolverTest {

    class Single {
        private val context = TestContext(Configuration(), FormatResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        private val context = TestContext(Configuration(), FormatResolver())

        @Parameterized.Parameter(0)
        lateinit var clazz: KClass<*>

        @Test
        fun `creates instance`() {
            val result = context.resolve(clazz)

            assertTrue {
                clazz.isInstance(result)
            }
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                (context.resolve(clazz) as Format)
            }
        }

        @Test
        fun `Uses seeded random`() {
            val value1 = context.seedRandom().resolve(clazz) as Format
            val value2 = context.seedRandom().resolve(clazz) as Format

            assertEquals(value1, value2)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(NumberFormat::class),
                arrayOf(DecimalFormat::class),
                arrayOf(DateFormat::class),
                arrayOf(SimpleDateFormat::class)
            )
        }
    }
}
