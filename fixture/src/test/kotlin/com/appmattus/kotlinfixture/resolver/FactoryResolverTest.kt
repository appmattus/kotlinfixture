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
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.typeOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FactoryResolverTest {

    @Test
    fun `Unresolved returned when no mapping found`() {
        val context = TestContext(Configuration(), FactoryResolver())

        assertEquals(Unresolved, context.resolve(typeOf<Number>()))
    }

    @Test
    fun `Factory returned when non-null mapping found for non-null`() {
        val configuration = ConfigurationBuilder().apply {
            factory<Number> { 12 }
        }.build()
        val context = TestContext(configuration, FactoryResolver())

        assertEquals(12, context.resolve(typeOf<Number>()))
    }

    @Test
    fun `Factory mapping can return random value`() {
        val configuration = ConfigurationBuilder().apply {
            factory<Number> { Random.nextInt(1, 5) }
        }.build()
        val context = TestContext(configuration, FactoryResolver())

        repeat(100) {
            assertTrue(context.resolve(typeOf<Number>()) in 1..5)
        }
    }

    @Test
    fun `Unresolved returned when non-nullable requested and only nullable factory found`() {
        val configuration = ConfigurationBuilder().apply {
            factory<Number?> { 12 }
        }.build()
        val context = TestContext(configuration, FactoryResolver())

        assertEquals(Unresolved, context.resolve(typeOf<Number>()))
    }

    @Test
    fun `Factory returned when nullable mapping found for nullable`() {
        val configuration = ConfigurationBuilder().apply {
            factory<Number?> { 12 }
        }.build()
        val context = TestContext(configuration, FactoryResolver())

        assertEquals(12, context.resolve(typeOf<Number?>()))
    }

    @Test
    fun `Random nullability returned when non-null mapping found for nullable`() {
        val configuration = ConfigurationBuilder().apply {
            factory<Number> { 12 }
        }.build()
        val context = TestContext(configuration, FactoryResolver())

        assertIsRandom {
            context.resolve(typeOf<Number?>()) == null
        }
    }
}
