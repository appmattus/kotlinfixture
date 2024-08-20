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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SealedClassResolverTest {

    private val context = TestContext(Configuration(), SealedClassResolver())

    private val contextWithTestResolver = context.copy(resolver = CompositeResolver(context.resolver, TestResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    sealed class EmptySealedClass

    @Test
    fun `Sealed class with no subclasses returns Unresolved`() {
        val result = context.resolve(EmptySealedClass::class)

        assertTrue(result is Unresolved)
    }

    sealed class SingleSealedClass {
        object OnlySubclass : SingleSealedClass()
    }

    @Test
    fun `Sealed class with one subclass returns OnlySubclass`() {
        val result = contextWithTestResolver.resolve(SingleSealedClass::class)

        assertEquals(SingleSealedClass.OnlySubclass::class, result)
    }

    sealed class MultiSealedClass {
        object SubclassA : MultiSealedClass()
        object SubclassB : MultiSealedClass()
    }

    @Test
    fun `Sealed class with multiple subclass returns random value`() {
        assertIsRandom {
            contextWithTestResolver.resolve(MultiSealedClass::class)
        }
    }

    @Test
    fun `Sealed class with multiple subclass returns one of the subclasses`() {
        val result = contextWithTestResolver.resolve(MultiSealedClass::class)

        assertTrue {
            result == MultiSealedClass.SubclassA::class || result == MultiSealedClass.SubclassB::class
        }
    }
}
