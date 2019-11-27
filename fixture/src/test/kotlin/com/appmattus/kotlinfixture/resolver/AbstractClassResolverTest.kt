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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbstractClassResolverTest {

    private val context = TestContext(Configuration(), AbstractClassResolver())

    private val contextWithTestResolver = context.copy(resolver = CompositeResolver(context.resolver, TestResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    abstract class EmptyAbstractClass

    @Test
    fun `Abstract class with no subclasses returns Unresolved`() {
        val result = context.resolve(EmptyAbstractClass::class)

        assertEquals(Unresolved, result)
    }

    @Suppress("UnnecessaryAbstractClass")
    abstract class SingleAbstractClass {
        object OnlySubclass : SingleAbstractClass()
    }

    @Test
    fun `Abstract class with one subclass returns OnlySubclass`() {
        val result = contextWithTestResolver.resolve(SingleAbstractClass::class)

        assertEquals(SingleAbstractClass.OnlySubclass::class, result)
    }

    @Suppress("unused", "UnnecessaryAbstractClass")
    abstract class MultiAbstractClass {
        object SubclassA : MultiAbstractClass()
        object SubclassB : MultiAbstractClass()
    }

    @Test
    fun `Abstract class with multiple subclass returns random value`() {
        assertIsRandom {
            contextWithTestResolver.resolve(MultiAbstractClass::class)
        }
    }

    @Test
    fun `Abstract class with multiple subclass returns one of the subclasses`() {
        val result = contextWithTestResolver.resolve(MultiAbstractClass::class)

        assertTrue {
            result == MultiAbstractClass.SubclassA::class || result == MultiAbstractClass.SubclassB::class
        }
    }

    abstract class EmptyInterfaceClass

    @Test
    fun `Interface with no subclasses returns Unresolved`() {
        val result = context.resolve(EmptyInterfaceClass::class)

        assertEquals(Unresolved, result)
    }

    interface SingleInterfaceClass {
        object OnlySubclass : SingleInterfaceClass
    }

    @Test
    fun `Interface with one subclass returns OnlySubclass`() {
        val result = contextWithTestResolver.resolve(SingleInterfaceClass::class)

        assertEquals(SingleInterfaceClass.OnlySubclass::class, result)
    }

    @Suppress("unused", "UnnecessaryAbstractClass")
    abstract class MultiInterfaceClass {
        object SubclassA : MultiInterfaceClass()
        object SubclassB : MultiInterfaceClass()
    }

    @Test
    fun `Interface with multiple subclass returns random value`() {
        assertIsRandom {
            contextWithTestResolver.resolve(MultiInterfaceClass::class)
        }
    }

    @Test
    fun `Interface with multiple subclass returns one of the subclasses`() {
        val result = contextWithTestResolver.resolve(MultiInterfaceClass::class)

        assertTrue {
            result == MultiInterfaceClass.SubclassA::class || result == MultiInterfaceClass.SubclassB::class
        }
    }
}
