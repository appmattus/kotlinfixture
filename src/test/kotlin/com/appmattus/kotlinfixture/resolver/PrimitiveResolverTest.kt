package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PrimitiveResolverTest {
    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = PrimitiveResolver()
    }

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Boolean class returns boolean`() {
        val result = context.resolve(Boolean::class)

        assertNotNull(result)
        assertEquals(Boolean::class, result::class)
    }

    @Test
    fun `Random Boolean values returned`() {
        assertIsRandom {
            context.resolve(Boolean::class)
        }
    }

    @Test
    fun `Byte class returns byte`() {
        val result = context.resolve(Byte::class)

        assertNotNull(result)
        assertEquals(Byte::class, result::class)
    }

    @Test
    fun `Random Byte values returned`() {
        assertIsRandom {
            context.resolve(Byte::class)
        }
    }

    @Test
    fun `Double class returns double`() {
        val result = context.resolve(Double::class)

        assertNotNull(result)
        assertEquals(Double::class, result::class)
    }

    @Test
    fun `Random Double values returned`() {
        assertIsRandom {
            context.resolve(Double::class)
        }
    }

    @Test
    fun `Float class returns float`() {
        val result = context.resolve(Float::class)

        assertNotNull(result)
        assertEquals(Float::class, result::class)
    }

    @Test
    fun `Random Float values returned`() {
        assertIsRandom {
            context.resolve(Float::class)
        }
    }

    @Test
    fun `Int class returns int`() {
        val result = context.resolve(Int::class)

        assertNotNull(result)
        assertEquals(Int::class, result::class)
    }

    @Test
    fun `Random Int values returned`() {
        assertIsRandom {
            context.resolve(Int::class)
        }
    }

    @Test
    fun `Long class returns long`() {
        val result = context.resolve(Long::class)

        assertNotNull(result)
        assertEquals(Long::class, result::class)
    }

    @Test
    fun `Random Long values returned`() {
        assertIsRandom {
            context.resolve(Long::class)
        }
    }


    @Test
    fun `Short class returns short`() {
        val result = context.resolve(Short::class)

        assertNotNull(result)
        assertEquals(Short::class, result::class)
    }

    @Test
    fun `Random Short values returned`() {
        assertIsRandom {
            context.resolve(Short::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UByte class returns ubyte`() {
        val result = context.resolve(UByte::class)

        assertNotNull(result)
        assertEquals(UByte::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UByte values returned`() {
        assertIsRandom {
            context.resolve(UByte::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UInt class returns uint`() {
        val result = context.resolve(UInt::class)

        assertNotNull(result)
        assertEquals(UInt::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UInt values returned`() {
        assertIsRandom {
            context.resolve(UInt::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `ULong class returns ulong`() {
        val result = context.resolve(ULong::class)

        assertNotNull(result)
        assertEquals(ULong::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random ULong values returned`() {
        assertIsRandom {
            context.resolve(ULong::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UShort class returns ushort`() {
        val result = context.resolve(UShort::class)

        assertNotNull(result)
        assertEquals(UShort::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UShort values returned`() {
        assertIsRandom {
            context.resolve(UShort::class)
        }
    }
}
