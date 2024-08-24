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

import io.github.detomarco.kotlinfixture.FixtureTestJavaClass
import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.config.Configuration
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.decorator.constructor.ConstructorStrategy
import io.github.detomarco.kotlinfixture.decorator.constructor.GreedyConstructorStrategy
import io.github.detomarco.kotlinfixture.decorator.constructor.ModestConstructorStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ClassResolverTest {
    private val context = TestContext(
        Configuration(),
        CompositeResolver(
            PrimitiveResolver(),
            StringResolver(),
            KTypeResolver(),
            ClassResolver(),
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
    fun `Class returns Unresolved when not able to resolve constructor`() {
        val context = TestContext(
            Configuration(),
            ClassResolver()
        )

        val result = context.resolve(SingleConstructor::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Class with single constructor creates instance`() {
        repeat(100) {
            val result = context.resolve(SingleConstructor::class)

            assertNotNull(result)
            assertEquals(SingleConstructor::class, result!!::class)
        }
    }

    @Test
    fun `Class with single constructor generates random content`() {
        repeat(100) {
            assertIsRandom {
                (context.resolve(SingleConstructor::class) as SingleConstructor).value
            }
        }
    }

    @Test
    fun `Constructor parameter can be overridden`() {
        repeat(100) {
            val context = context.copy(
                configuration = ConfigurationBuilder().apply {
                    property(SingleConstructor::value) { "custom" }
                }.build()
            )

            val result = context.resolve(SingleConstructor::class) as SingleConstructor
            assertEquals("custom", result.value)
        }
    }

    @Test
    fun `Class with multiple constructors picks one at random`() {
        repeat(100) {
            assertIsRandom {
                val result = context.resolve(MultipleConstructors::class) as MultipleConstructors
                result.constructorCalled
            }
        }
    }

    @Test
    fun `Class with multiple constructors uses seeded random`() {
        val value1 =
            (context.seedRandom().resolve(MultipleConstructors::class) as MultipleConstructors).constructorCalled
        val value2 =
            (context.seedRandom().resolve(MultipleConstructors::class) as MultipleConstructors).constructorCalled

        assertEquals(value1, value2)
    }

    @Test
    fun `Class with multiple constructors using modest strategy picks primary constructor`() {
        val strategy = context.copy(
            configuration = context.configuration.copy(
                strategies = mapOf(ConstructorStrategy::class to ModestConstructorStrategy)
            )
        )

        val value = (strategy.resolve(MultipleConstructors::class) as MultipleConstructors).constructorCalled

        assertEquals("primary", value)
    }

    @Test
    fun `Class with multiple constructors using greedy strategy picks secondary constructor`() {
        val strategy = context.copy(
            configuration = context.configuration.copy(
                strategies = mapOf(ConstructorStrategy::class to GreedyConstructorStrategy)
            )
        )

        val value = (strategy.resolve(MultipleConstructors::class) as MultipleConstructors).constructorCalled

        assertEquals("secondary", value)
    }

    @Test
    fun `Class with mutable parameter is set at random`() {
        repeat(100) {
            assertIsRandom {
                val result = context.resolve(MutableParameter::class) as MutableParameter
                result.parameter
            }
        }
    }

    @Test
    fun `Mutable parameter can be overridden`() {
        repeat(100) {
            val context = context.copy(
                configuration = ConfigurationBuilder().apply {
                    property(MutableParameter::parameter) { "custom" }
                }.build()
            )

            val result = context.resolve(MutableParameter::class) as MutableParameter
            assertEquals("custom", result.parameter)
        }
    }

    @Test
    fun `Parameter unset if name matches constructor property name`() {
        repeat(100) {
            val result = context.resolve(MatchingNames::class) as MatchingNames
            assertFalse(result.isInitialised)
        }
    }

    @Test
    fun `Constructs Java class with random constructor value`() {
        repeat(100) {
            assertIsRandom {
                (context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass).constructor
            }
        }
    }

    @Test
    fun `Constructs Java class with random setter value`() {
        repeat(100) {
            assertIsRandom {
                (context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass).mutable
            }
        }
    }

    @Test
    fun `Can override Java constructor arg`() {
        repeat(100) {
            val context = context.copy(
                configuration = ConfigurationBuilder().apply {
                    property<FixtureTestJavaClass, String>("arg0") { "custom" }
                }.build()
            )

            val result = context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass
            assertEquals("custom", result.constructor)
        }
    }

    @Test
    fun `Can override Java setter`() {
        repeat(100) {
            val context = context.copy(
                configuration = ConfigurationBuilder().apply {
                    property<String>(FixtureTestJavaClass::setMutable) { "custom" }
                }.build()
            )

            val result = context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass
            assertEquals("custom", result.mutable)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    class MatchingNames(number: Int) {
        lateinit var number: String

        val isInitialised: Boolean
            get() = ::number.isInitialized
    }

    data class SingleConstructor(val value: String)

    @Suppress("unused", "UNUSED_PARAMETER")
    class MultipleConstructors {
        val constructorCalled: String

        constructor() {
            constructorCalled = "primary"
        }

        constructor(value: String) {
            constructorCalled = "secondary"
        }
    }

    class MutableParameter {
        lateinit var parameter: String
    }
}
