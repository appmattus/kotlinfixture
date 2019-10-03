package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.util.EnumSet
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumSetResolverTest {
    private val context = TestContext(Configuration(), CompositeResolver(EnumSetResolver(), EnumResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns empty EnumSet`() {
        @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
        val result = context.resolve(typeOf<EnumSet<EmptyEnumClass>>())

        assertEquals(EnumSet.noneOf(EmptyEnumClass::class.java), result)
    }

    enum class SingleEnumClass {
        @Suppress("unused")
        OnlyValue
    }

    @Test
    fun `Enum with one value returns either empty set or set with one value`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            context.resolve(typeOf<EnumSet<SingleEnumClass>>())
        }
    }

    @Test
    fun `randomly returns null for nullable type`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            context.resolve(typeOf<EnumSet<SingleEnumClass>?>()) == null
        }
    }

    @Test
    fun `Enum with one value returns random length set`() {
        assertIsRandom {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            (context.resolve(typeOf<EnumSet<SingleEnumClass>>()) as EnumSet<*>).size
        }
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
            context.resolve(typeOf<EnumSet<MultiEnumClass>>())
        }
    }
}
