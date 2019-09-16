package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PrimitiveResolverTest {
    private val resolver = PrimitiveResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Boolean class returns boolean`() {
        val result = resolver.resolve(Boolean::class, resolver)

        assertNotNull(result)
        assertEquals(Boolean::class, result::class)
    }

    @Test
    fun `Random Boolean values returned`() {
        assertIsRandom {
            resolver.resolve(Boolean::class, resolver)
        }
    }

    @Test
    fun `Byte class returns boolean`() {
        val result = resolver.resolve(Byte::class, resolver)

        assertNotNull(result)
        assertEquals(Byte::class, result::class)
    }

    @Test
    fun `Random Byte values returned`() {
        assertIsRandom {
            resolver.resolve(Byte::class, resolver)
        }
    }

    @Test
    fun `Double class returns boolean`() {
        val result = resolver.resolve(Double::class, resolver)

        assertNotNull(result)
        assertEquals(Double::class, result::class)
    }

    @Test
    fun `Random Double values returned`() {
        assertIsRandom {
            resolver.resolve(Double::class, resolver)
        }
    }

    @Test
    fun `Float class returns boolean`() {
        val result = resolver.resolve(Float::class, resolver)

        assertNotNull(result)
        assertEquals(Float::class, result::class)
    }

    @Test
    fun `Random Float values returned`() {
        assertIsRandom {
            resolver.resolve(Float::class, resolver)
        }
    }

    @Test
    fun `Int class returns boolean`() {
        val result = resolver.resolve(Int::class, resolver)

        assertNotNull(result)
        assertEquals(Int::class, result::class)
    }

    @Test
    fun `Random Int values returned`() {
        assertIsRandom {
            resolver.resolve(Int::class, resolver)
        }
    }

    @Test
    fun `Long class returns boolean`() {
        val result = resolver.resolve(Long::class, resolver)

        assertNotNull(result)
        assertEquals(Long::class, result::class)
    }

    @Test
    fun `Random Long values returned`() {
        assertIsRandom {
            resolver.resolve(Long::class, resolver)
        }
    }


    @Test
    fun `Short class returns boolean`() {
        val result = resolver.resolve(Short::class, resolver)

        assertNotNull(result)
        assertEquals(Short::class, result::class)
    }

    @Test
    fun `Random Short values returned`() {
        assertIsRandom {
            resolver.resolve(Short::class, resolver)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UByte class returns boolean`() {
        val result = resolver.resolve(UByte::class, resolver)

        assertNotNull(result)
        assertEquals(UByte::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UByte values returned`() {
        assertIsRandom {
            resolver.resolve(UByte::class, resolver)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UInt class returns boolean`() {
        val result = resolver.resolve(UInt::class, resolver)

        assertNotNull(result)
        assertEquals(UInt::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UInt values returned`() {
        assertIsRandom {
            resolver.resolve(UInt::class, resolver)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `ULong class returns boolean`() {
        val result = resolver.resolve(ULong::class, resolver)

        assertNotNull(result)
        assertEquals(ULong::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random ULong values returned`() {
        assertIsRandom {
            resolver.resolve(ULong::class, resolver)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UShort class returns boolean`() {
        val result = resolver.resolve(UShort::class, resolver)

        assertNotNull(result)
        assertEquals(UShort::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UShort values returned`() {
        assertIsRandom {
            resolver.resolve(UShort::class, resolver)
        }
    }
}
