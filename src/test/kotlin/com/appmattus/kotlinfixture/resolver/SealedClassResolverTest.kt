package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SealedClassResolverTest {
    private val resolver = SealedClassResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    sealed class EmptySealedClass

    @Test
    fun `Sealed class with no subclasses returns Unresolved`() {
        val result = resolver.resolve(EmptySealedClass::class, resolver)

        assertEquals(Unresolved, result)
    }

    sealed class SingleSealedClass {
        object OnlySubclass : SingleSealedClass()
    }

    @Test
    fun `Sealed class with one subclass returns OnlySubclass`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(SingleSealedClass::class, testResolver)

        assertEquals(SingleSealedClass.OnlySubclass::class, result)
    }

    sealed class MultiSealedClass {
        object SubclassA : MultiSealedClass()
        object SubclassB : MultiSealedClass()
    }

    @Test
    fun `Sealed class with multiple subclass returns random value`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        assertIsRandom {
            resolver.resolve(MultiSealedClass::class, testResolver)
        }
    }

    @Test
    fun `Sealed class with multiple subclass returns one of the subclasses`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return obj
            }
        }

        val result = resolver.resolve(MultiSealedClass::class, testResolver)

        assertTrue {
            result == MultiSealedClass.SubclassA::class || result == MultiSealedClass.SubclassB::class
        }
    }
}
