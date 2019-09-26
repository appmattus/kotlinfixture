package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SealedClassResolverTest {

    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = SealedClassResolver()
    }

    private val contextWithTestResolver = object : Context {
        override val configuration = Configuration()
        override val rootResolver = CompositeResolver(SealedClassResolver(), TestResolver())
    }

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    sealed class EmptySealedClass

    @Test
    fun `Sealed class with no subclasses returns Unresolved`() {
        val result = context.resolve(EmptySealedClass::class)

        assertEquals(Unresolved, result)
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
