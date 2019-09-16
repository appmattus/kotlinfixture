package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbstractClassResolverTest {
    private val resolver = AbstractClassResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    abstract class EmptyAbstractClass

    @Test
    fun `Abstract class with no subclasses returns Unresolved`() {
        val result = resolver.resolve(EmptyAbstractClass::class, resolver)

        assertEquals(Unresolved, result)
    }

    abstract class SingleAbstractClass {
        object OnlySubclass : SingleAbstractClass()
    }

    @Test
    fun `Abstract class with one subclass returns OnlySubclass`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(SingleAbstractClass::class, testResolver)

        assertEquals(SingleAbstractClass.OnlySubclass::class, result)
    }

    @Suppress("unused")
    abstract class MultiAbstractClass {
        object SubclassA : MultiAbstractClass()
        object SubclassB : MultiAbstractClass()
    }

    @Test
    fun `Abstract class with multiple subclass returns random value`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        assertIsRandom {
            resolver.resolve(MultiAbstractClass::class, testResolver)
        }
    }

    @Test
    fun `Abstract class with multiple subclass returns one of the subclasses`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(MultiAbstractClass::class, testResolver)

        assertTrue {
            result == MultiAbstractClass.SubclassA::class || result == MultiAbstractClass.SubclassB::class
        }
    }

    abstract class EmptyInterfaceClass

    @Test
    fun `Interface with no subclasses returns Unresolved`() {
        val result = resolver.resolve(EmptyInterfaceClass::class, resolver)

        assertEquals(Unresolved, result)
    }

    interface SingleInterfaceClass {
        object OnlySubclass : SingleInterfaceClass
    }

    @Test
    fun `Interface with one subclass returns OnlySubclass`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(SingleInterfaceClass::class, testResolver)

        assertEquals(SingleInterfaceClass.OnlySubclass::class, result)
    }

    @Suppress("unused")
    abstract class MultiInterfaceClass {
        object SubclassA : MultiInterfaceClass()
        object SubclassB : MultiInterfaceClass()
    }

    @Test
    fun `Interface with multiple subclass returns random value`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        assertIsRandom {
            resolver.resolve(MultiInterfaceClass::class, testResolver)
        }
    }

    @Test
    fun `Interface with multiple subclass returns one of the subclasses`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(MultiInterfaceClass::class, testResolver)

        assertTrue {
            result == MultiInterfaceClass.SubclassA::class || result == MultiInterfaceClass.SubclassB::class
        }
    }
}
