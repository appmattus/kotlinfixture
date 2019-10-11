package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.FixtureTestJavaClass
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ClassResolverTest {
    private val context = TestContext(
        Configuration(),
        CompositeResolver(PrimitiveResolver(), StringResolver(), KTypeResolver(), ClassResolver(), KFunctionResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Class returns Unresolved when not able to resolve constructor`() {
        val context = TestContext(
            Configuration(),
            ClassResolver()
        )

        val result = context.resolve(SingleConstructor::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Class with single constructor creates instance`() {
        val result = context.resolve(SingleConstructor::class)

        assertNotNull(result)
        assertEquals(SingleConstructor::class, result::class)
    }

    @Test
    fun `Class with single constructor generates random content`() {
        assertIsRandom {
            (context.resolve(SingleConstructor::class) as SingleConstructor).value
        }
    }

    @Test
    fun `Constructor parameter can be overridden`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(SingleConstructor::class to mapOf("value" to { "custom" }))
            )
        )

        val result = context.resolve(SingleConstructor::class) as SingleConstructor
        assertEquals("custom", result.value)
    }

    @Test
    fun `Class with multiple constructors picks one at random`() {
        assertIsRandom {
            val result = context.resolve(MultipleConstructors::class) as MultipleConstructors
            result.constructorCalled
        }
    }

    @Test
    fun `Class with mutable parameter is set at random`() {
        assertIsRandom {
            val result = context.resolve(MutableParameter::class) as MutableParameter
            result.parameter
        }
    }

    @Test
    fun `Mutable parameter can be overridden`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(MutableParameter::class to mapOf("parameter" to { "custom" }))
            )
        )

        val result = context.resolve(MutableParameter::class) as MutableParameter
        assertEquals("custom", result.parameter)
    }

    @Test
    fun `Parameter unset if name matches constructor property name`() {
        val result = context.resolve(MatchingNames::class) as MatchingNames
        assertFalse(result.isInitialised)
    }

    @Test
    fun `Constructs Java class with random constructor value`() {
        assertIsRandom {
            (context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass).constructor
        }
    }

    @Test
    fun `Constructs Java class with random setter value`() {
        assertIsRandom {
            (context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass).mutable
        }
    }

    @Test
    fun `Can override Java constructor arg`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(FixtureTestJavaClass::class to mapOf("arg0" to { "custom" }))
            )
        )

        val result = context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass
        assertEquals("custom", result.constructor)
    }

    @Test
    fun `Can override Java setter`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(FixtureTestJavaClass::class to mapOf("setMutable" to { "custom" }))
            )
        )

        val result = context.resolve(FixtureTestJavaClass::class) as FixtureTestJavaClass
        assertEquals("custom", result.mutable)
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
