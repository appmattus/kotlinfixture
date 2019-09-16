package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnumResolverTest {
    private val resolver = EnumResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, testResolver)

        assertEquals(Unresolved, result)
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns Unresolved`() {
        val result = resolver.resolve(EmptyEnumClass::class, testResolver)

        assertEquals(Unresolved, result)
    }

    enum class SingleEnumClass {
        OnlyValue
    }

    @Test
    fun `Enum with one value returns OnlyValue`() {
        val result = resolver.resolve(SingleEnumClass::class, testResolver)

        assertEquals(SingleEnumClass.OnlyValue, result)
    }

    enum class MultiEnumClass {
        ValueA, ValueB
    }

    @Test
    fun `Enum with multiple values returns random value`() {
        assertIsRandom {
            resolver.resolve(MultiEnumClass::class, testResolver)
        }
    }

    @Test
    fun `Enum with multiple values returns one of the values`() {
        val result = resolver.resolve(MultiEnumClass::class, testResolver)

        assertTrue {
            result == MultiEnumClass.ValueA || result == MultiEnumClass.ValueB
        }
    }

    @Test
    fun `Enum order is cached`() {
        assertEquals(
            listOf(
                resolver.resolve(MultiEnumClass::class, testResolver),
                resolver.resolve(MultiEnumClass::class, testResolver)
            ),
            listOf(
                resolver.resolve(MultiEnumClass::class, testResolver),
                resolver.resolve(MultiEnumClass::class, testResolver)
            )
        )
    }

    private val testResolver = object : Resolver {
        override fun resolve(obj: Any?, resolver: Resolver): Any? = obj
    }
}
