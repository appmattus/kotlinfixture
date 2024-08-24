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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

class PrimitiveResolverTest {

    @Nested
    inner class Single {
        val context = TestContext(Configuration(), PrimitiveResolver())

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertTrue(result is Unresolved)
        }
    }

    val context = TestContext(Configuration(), PrimitiveResolver())

    @ParameterizedTest
    @MethodSource("data")
    fun `Returns correct type`(clazz: KClass<*>) {
        val result = context.resolve(clazz)

        assertNotNull(result)
        assertEquals(clazz, result!!::class)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Random values returned`(clazz: KClass<*>) {
        assertIsRandom {
            context.resolve(clazz)
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Uses seeded random`(clazz: KClass<*>) {
        val value1 = context.seedRandom().resolve(clazz)
        val value2 = context.seedRandom().resolve(clazz)

        assertEquals(value1, value2)
    }

    companion object {
        @JvmStatic
        @Suppress("EXPERIMENTAL_API_USAGE")
        fun data() = arrayOf(
            Arguments.of(Boolean::class),
            Arguments.of(Byte::class),
            Arguments.of(Double::class),
            Arguments.of(Float::class),
            Arguments.of(Int::class),
            Arguments.of(Long::class),
            Arguments.of(Short::class),

            Arguments.of(UByte::class),
            Arguments.of(UInt::class),
            Arguments.of(ULong::class),
            Arguments.of(UShort::class)
        )
    }
}
