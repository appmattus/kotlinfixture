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

package com.detomarco.kotlinfixture.decorator.optional

import com.detomarco.kotlinfixture.TestContext
import com.detomarco.kotlinfixture.assertIsRandom
import com.detomarco.kotlinfixture.config.ConfigurationBuilder
import com.detomarco.kotlinfixture.resolver.TestResolver
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OverrideOptionalStrategyTest {

    private val testContext = TestContext(ConfigurationBuilder().build(), TestResolver())

    data class DataClass(val optionalValue: String = "hello")

    @Test
    fun `Default strategy NeverOptionalStrategy returns false`(): Unit = with(testContext) {
        OverrideOptionalStrategy(NeverOptionalStrategy).apply {
            assertFalse {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy AlwaysOptionalStrategy returns true`(): Unit = with(testContext) {
        OverrideOptionalStrategy(AlwaysOptionalStrategy).apply {
            assertTrue {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy RandomlyOptionalStrategy returns random value`(): Unit = with(testContext) {
        OverrideOptionalStrategy(RandomlyOptionalStrategy).apply {
            assertIsRandom {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy not overridden when classOverrides doesn't match`(): Unit = with(testContext) {
        OverrideOptionalStrategy(
            defaultStrategy = NeverOptionalStrategy,
            classOverrides = mapOf(String::class to AlwaysOptionalStrategy)
        ).apply {
            assertFalse {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy overridden by classOverrides`(): Unit = with(testContext) {
        OverrideOptionalStrategy(
            defaultStrategy = NeverOptionalStrategy,
            classOverrides = mapOf(DataClass::class to AlwaysOptionalStrategy)
        ).apply {
            assertTrue {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy not overridden when propertyOverrides doesn't match`(): Unit = with(testContext) {
        OverrideOptionalStrategy(
            defaultStrategy = NeverOptionalStrategy,
            propertyOverrides = mapOf(DataClass::class to mapOf("unmatchedValue" to AlwaysOptionalStrategy))
        ).apply {
            assertFalse {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy overridden by propertyOverrides`(): Unit = with(testContext) {
        OverrideOptionalStrategy(
            defaultStrategy = NeverOptionalStrategy,
            propertyOverrides = mapOf(DataClass::class to mapOf("optionalValue" to AlwaysOptionalStrategy))
        ).apply {
            assertTrue {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }

    @Test
    fun `Default strategy overridden by propertyOverrides over classOverrides`(): Unit = with(testContext) {
        OverrideOptionalStrategy(
            defaultStrategy = NeverOptionalStrategy,
            classOverrides = mapOf(DataClass::class to AlwaysOptionalStrategy),
            propertyOverrides = mapOf(DataClass::class to mapOf("optionalValue" to RandomlyOptionalStrategy))
        ).apply {
            assertIsRandom {
                generateAsOptional(DataClass::class, "optionalValue")
            }
        }
    }
}
