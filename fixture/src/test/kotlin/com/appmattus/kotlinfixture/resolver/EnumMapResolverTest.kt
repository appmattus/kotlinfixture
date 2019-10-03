package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.util.EnumMap
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumMapResolverTest {

    enum class SingleEnumClass {
        @Suppress("unused")
        A
    }

    val context = TestContext(
        Configuration(),
        CompositeResolver(EnumMapResolver(), EnumResolver(), StringResolver(), KTypeResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Unknown value type parameter returns Unresolved`() {
        @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
        val result = context.resolve(typeOf<EnumMap<SingleEnumClass, Number>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Length is random`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR", "UNCHECKED_CAST")
            (context.resolve(typeOf<EnumMap<SingleEnumClass, String>>()) as EnumMap<SingleEnumClass, String>).size
        }
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns empty EnumMap`() {
        @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
        val result = context.resolve(typeOf<EnumMap<EmptyEnumClass, String>>())

        assertEquals(EnumMap<EmptyEnumClass, String>(EmptyEnumClass::class.java), result)
    }

    enum class MultiEnumClass {
        @Suppress("unused")
        ValueA,
        @Suppress("unused")
        ValueB
    }

    @Test
    fun `Enum with multiple values returns random value`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            context.resolve(typeOf<EnumMap<MultiEnumClass, String>>())
        }
    }

    @Test
    fun `Enum with multiple values is randomly empty`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            (context.resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>).isEmpty()
        }
    }
}
