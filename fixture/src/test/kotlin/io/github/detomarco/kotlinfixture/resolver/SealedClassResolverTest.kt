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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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
        data object OnlySubclass : SingleSealedClass()
    }

    @Test
    fun `Sealed class with one subclass returns OnlySubclass`() {
        val result = contextWithTestResolver.resolve(SingleSealedClass::class)

        assertEquals(SingleSealedClass.OnlySubclass::class, result)
    }

    sealed class MultiSealedClass {
        data object SubclassA : MultiSealedClass()
        data object SubclassB : MultiSealedClass()
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
