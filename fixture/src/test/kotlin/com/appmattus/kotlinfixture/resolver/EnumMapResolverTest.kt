package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.typeOf
import java.util.EnumMap
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
        val result = context.resolve(typeOf<EnumMap<SingleEnumClass, Number>>())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Length is random`() {
        assertIsRandom {
            @Suppress("UNCHECKED_CAST")
            (context.resolve(typeOf<EnumMap<SingleEnumClass, String>>()) as EnumMap<SingleEnumClass, String>).size
        }
    }

    enum class EmptyEnumClass

    @Test
    fun `Enum with no values returns empty EnumMap`() {
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
            context.resolve(typeOf<EnumMap<MultiEnumClass, String>>())
        }
    }

    @Test
    fun `Random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<EnumMap<MultiEnumClass, String>?>()) == null
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>
        val value2 = context.seedRandom().resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>

        assertEquals(value1, value2)
    }

    @Test
    fun `Enum with multiple values is randomly empty`() {
        assertIsRandom {
            (context.resolve(typeOf<EnumMap<MultiEnumClass, String>>()) as EnumMap<*, *>).isEmpty()
        }
    }
}
