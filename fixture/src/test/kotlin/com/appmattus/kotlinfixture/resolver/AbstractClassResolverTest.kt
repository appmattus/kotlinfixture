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

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbstractClassResolverTest {

    private val context = TestContext(Configuration(), AbstractClassResolver())

    private val contextWithTestResolver = context.copy(resolver = CompositeResolver(context.resolver, TestResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    abstract class EmptyAbstractClass

    @Test
    fun `Abstract class with no subclasses returns Unresolved`() {
        val result = context.resolve(EmptyAbstractClass::class)

        assertTrue(result is Unresolved)
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

        assertTrue(result is Unresolved)
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

    @Test
    fun `Abstract class unknown to the class scan returns Unresolved`() {
        // Defined at runtime, so the class scan has never seen it
        val clazz = GeneratedClassLoader().defineAbstractClass(
            "com.appmattus.kotlinfixture.resolver.GeneratedAbstractClass"
        )

        val result = context.resolve(clazz.kotlin)

        assertTrue(result is Unresolved)
    }

    @Suppress("unused", "UnnecessaryAbstractClass")
    abstract class PartlyUnusableAbstractClass {
        object Unusable : PartlyUnusableAbstractClass()
        object Usable : PartlyUnusableAbstractClass()
    }

    @Test
    fun `Subclass that kotlin-reflect cannot use is skipped`() {
        val context = contextThrowingFor(PartlyUnusableAbstractClass.Unusable::class.java) {
            KotlinReflectionInternalError("Unable to reflect")
        }

        repeat(SAMPLES) {
            assertEquals(PartlyUnusableAbstractClass.Usable::class, context.resolve(PartlyUnusableAbstractClass::class))
        }
    }

    @Test
    fun `Subclass with a missing dependency is skipped`() {
        val context = contextThrowingFor(PartlyUnusableAbstractClass.Unusable::class.java) {
            NoClassDefFoundError("Missing dependency")
        }

        repeat(SAMPLES) {
            assertEquals(PartlyUnusableAbstractClass.Usable::class, context.resolve(PartlyUnusableAbstractClass::class))
        }
    }

    @Test
    fun `Abstract class whose subclasses are all unusable returns Unresolved`() {
        val subclasses = setOf(PartlyUnusableAbstractClass.Unusable::class, PartlyUnusableAbstractClass.Usable::class)
        val context = context.copy(
            resolver = CompositeResolver(
                context.resolver,
                ThrowingResolver(error = { NoClassDefFoundError("Missing dependency") }) { it in subclasses }
            )
        )

        val result = context.resolve(PartlyUnusableAbstractClass::class)

        assertTrue(result is Unresolved)
    }

    private fun contextThrowingFor(clazz: Class<*>, error: () -> Throwable) = context.copy(
        resolver = CompositeResolver(
            context.resolver,
            ThrowingResolver(error) { it == clazz.kotlin },
            TestResolver()
        )
    )

    /** Throws [error] when asked to resolve a class that matches [predicate] */
    private class ThrowingResolver(
        private val error: () -> Throwable,
        private val predicate: (Any) -> Boolean
    ) : Resolver {
        override fun resolve(
            context: Context,
            obj: Any
        ): Any = if (predicate(obj)) throw error() else Unresolved.Unhandled
    }

    private class GeneratedClassLoader : ClassLoader(AbstractClassResolverTest::class.java.classLoader) {

        /** Defines an empty public abstract class from minimal hand-written bytecode */
        fun defineAbstractClass(name: String): Class<*> {
            val bytes = ByteArrayOutputStream().also { output ->
                DataOutputStream(output).use {
                    it.writeInt(CLASS_FILE_MAGIC)
                    it.writeShort(0) // Minor version
                    it.writeShort(JAVA_8_MAJOR_VERSION)

                    // Constant pool: the class and its superclass, each a class entry referring to its name
                    it.writeShort(CONSTANT_POOL_COUNT)
                    it.writeByte(CONSTANT_UTF8)
                    it.writeUTF(name.replace('.', '/'))
                    it.writeByte(CONSTANT_CLASS)
                    it.writeShort(1)
                    it.writeByte(CONSTANT_UTF8)
                    it.writeUTF("java/lang/Object")
                    it.writeByte(CONSTANT_CLASS)
                    it.writeShort(3)

                    it.writeShort(ACC_PUBLIC or ACC_SUPER or ACC_ABSTRACT)
                    it.writeShort(2) // This class
                    it.writeShort(4) // Superclass
                    it.writeShort(0) // Interfaces
                    it.writeShort(0) // Fields
                    it.writeShort(0) // Methods
                    it.writeShort(0) // Attributes
                }
            }.toByteArray()

            return defineClass(name, bytes, 0, bytes.size)
        }

        private companion object {
            private const val CLASS_FILE_MAGIC = 0xCAFEBABE.toInt()
            private const val JAVA_8_MAJOR_VERSION = 52
            private const val CONSTANT_POOL_COUNT = 5
            private const val CONSTANT_UTF8 = 1
            private const val CONSTANT_CLASS = 7
            private const val ACC_PUBLIC = 0x0001
            private const val ACC_SUPER = 0x0020
            private const val ACC_ABSTRACT = 0x0400
        }
    }

    private companion object {
        private const val SAMPLES = 50
    }
}
