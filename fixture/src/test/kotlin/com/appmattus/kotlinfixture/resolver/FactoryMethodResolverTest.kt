package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.FactoryMethodJavaClass
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FactoryMethodResolverTest {
    private val context = TestContext(
        Configuration(),
        CompositeResolver(
            PrimitiveResolver(),
            StringResolver(),
            KTypeResolver(),
            FactoryMethodResolver(),
            KFunctionResolver()
        )
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Class returns Unresolved when not able to resolve factory method`() {
        val context = TestContext(
            Configuration(),
            FactoryMethodResolver()
        )

        val result = context.resolve(SingleFactoryMethod::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Class with single factory method creates instance`() {
        val result = context.resolve(SingleFactoryMethod::class)

        assertNotNull(result)
        assertEquals(SingleFactoryMethod::class, result::class)
    }

    @Test
    fun `Class with single factory method generates random content`() {
        assertIsRandom {
            (context.resolve(SingleFactoryMethod::class) as SingleFactoryMethod).value
        }
    }

    @Test
    fun `Factory method parameter can be overridden`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(SingleFactoryMethod::class to mapOf("value" to { "custom" }))
            )
        )

        val result = context.resolve(SingleFactoryMethod::class) as SingleFactoryMethod
        assertEquals("custom", result.value)
    }

    @Test
    fun `Class with multiple constructors picks one at random`() {
        assertIsRandom {
            val result = context.resolve(MultipleFactoryMethods::class) as MultipleFactoryMethods
            result.factoryMethodCalled
        }
    }

    @Test
    fun `Class with mutable parameter is set at random`() {
        assertIsRandom {
            val result =
                context.resolve(MutableParameter::class) as MutableParameter
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
    fun `Constructs Java class with random constructor value`() {
        assertIsRandom {
            (context.resolve(FactoryMethodJavaClass::class) as FactoryMethodJavaClass).constructor
        }
    }

    @Test
    fun `Constructs Java class with random setter value`() {
        assertIsRandom {
            (context.resolve(FactoryMethodJavaClass::class) as FactoryMethodJavaClass).mutable
        }
    }

    @Test
    fun `Can override Java constructor arg`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(FactoryMethodJavaClass::class to mapOf("arg0" to { "custom" }))
            )
        )

        val result = context.resolve(FactoryMethodJavaClass::class) as FactoryMethodJavaClass
        assertEquals("custom", result.constructor)
    }

    @Test
    fun `Can override Java setter`() {
        val context = context.copy(
            configuration = Configuration(
                properties = mapOf(FactoryMethodJavaClass::class to mapOf("setMutable" to { "custom" }))
            )
        )

        val result = context.resolve(FactoryMethodJavaClass::class) as FactoryMethodJavaClass
        assertEquals("custom", result.mutable)
    }

    @Suppress("UNUSED_PARAMETER")
    class MatchingNames(number: Int) {
        lateinit var number: String

        val isInitialised: Boolean
            get() = ::number.isInitialized

        companion object {
            fun create(value: Int) = MatchingNames(value)
        }
    }

    class SingleFactoryMethod private constructor(val value: String) {
        companion object {
            fun create(value: String) = SingleFactoryMethod(value)
        }
    }

    class MultipleFactoryMethods private constructor(val value: String, val factoryMethodCalled: String) {
        companion object {
            fun create() = MultipleFactoryMethods("default", "noParams")
            fun create(value: String) = MultipleFactoryMethods(value, "oneParam")
        }
    }

    class MutableParameter private constructor() {
        lateinit var parameter: String

        companion object {
            fun create() = MutableParameter()
        }
    }
}
