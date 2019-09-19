package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ClassResolverTest {
    private val configuration = Configuration()
    private val resolver = ClassResolver(configuration)
    private val testResolver = CompositeResolver(
        KFunctionResolver(configuration),
        PrimitiveResolver(),
        StringResolver(),
        KTypeResolver()
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Class with single constructor creates instance`() {
        val result = resolver.resolve(SingleConstructor::class, testResolver)

        assertNotNull(result)
        assertEquals(SingleConstructor::class, result::class)
    }

    @Test
    fun `Class with multiple constructors picks one at random`() {
        assertIsRandom {
            val result = resolver.resolve(MultipleConstructors::class, testResolver) as MultipleConstructors
            result.constructorCalled
        }
    }

    @Test
    fun `Class with mutable parameter is set at random`() {
        assertIsRandom {
            val result = resolver.resolve(MutableParameter::class, testResolver) as MutableParameter
            result.parameter
        }
    }

    @Test
    fun `Mutable parameter can be overridden`() {
        val configuration = Configuration(properties = mapOf(MutableParameter::class to mapOf("parameter" to "custom")))
        val resolver = ClassResolver(configuration)

        val result = resolver.resolve(MutableParameter::class, testResolver) as MutableParameter
        assertEquals("custom", result.parameter)
    }

    @Test
    fun `Parameter unset if name matches constructor property name`() {
        val result = resolver.resolve(MatchingNames::class, testResolver) as MatchingNames
        assertFalse(result.isInitialised)
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
