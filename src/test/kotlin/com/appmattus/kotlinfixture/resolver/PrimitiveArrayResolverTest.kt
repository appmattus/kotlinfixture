package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PrimitiveArrayResolverTest {
    private val resolver = PrimitiveArrayResolver(Configuration())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, PrimitiveResolver())

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BooleanArray class returns boolean array`() {
        val result = resolver.resolve(BooleanArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(BooleanArray::class, result::class)
    }

    @Test
    fun `Random BooleanArray values returned`() {
        assertIsRandom {
            resolver.resolve(BooleanArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `ByteArray class returns byte array`() {
        val result = resolver.resolve(ByteArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(ByteArray::class, result::class)
    }

    @Test
    fun `Random ByteArray values returned`() {
        assertIsRandom {
            resolver.resolve(ByteArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `DoubleArray class returns double array`() {
        val result = resolver.resolve(DoubleArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(DoubleArray::class, result::class)
    }

    @Test
    fun `Random DoubleArray values returned`() {
        assertIsRandom {
            resolver.resolve(DoubleArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `FloatArray class returns float array`() {
        val result = resolver.resolve(FloatArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(FloatArray::class, result::class)
    }

    @Test
    fun `Random FloatArray values returned`() {
        assertIsRandom {
            resolver.resolve(FloatArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `IntArray class returns int array`() {
        val result = resolver.resolve(IntArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(IntArray::class, result::class)
    }

    @Test
    fun `Random IntArray values returned`() {
        assertIsRandom {
            resolver.resolve(IntArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `LongArray class returns long array`() {
        val result = resolver.resolve(LongArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(LongArray::class, result::class)
    }

    @Test
    fun `Random LongArray values returned`() {
        assertIsRandom {
            resolver.resolve(LongArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `ShortArray class returns short array`() {
        val result = resolver.resolve(ShortArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(ShortArray::class, result::class)
    }

    @Test
    fun `Random ShortArray values returned`() {
        assertIsRandom {
            resolver.resolve(ShortArray::class, PrimitiveResolver())
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UByteArray class returns ubyte array`() {
        val result = resolver.resolve(UByteArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(UByteArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UByteArray values returned`() {
        assertIsRandom {
            resolver.resolve(UByteArray::class, PrimitiveResolver())
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UIntArray class returns unit array`() {
        val result = resolver.resolve(UIntArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(UIntArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UIntArray values returned`() {
        assertIsRandom {
            resolver.resolve(UIntArray::class, PrimitiveResolver())
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `ULongArray class returns ulong array`() {
        val result = resolver.resolve(ULongArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(ULongArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random ULongArray values returned`() {
        assertIsRandom {
            resolver.resolve(ULongArray::class, PrimitiveResolver())
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UShortArray class returns ushort array`() {
        val result = resolver.resolve(UShortArray::class, PrimitiveResolver())

        assertNotNull(result)
        assertEquals(UShortArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UShortArray values returned`() {
        assertIsRandom {
            resolver.resolve(UShortArray::class, PrimitiveResolver())
        }
    }

    @Test
    fun `Length of array matches configuration value of 3`() {
        val resolver = PrimitiveArrayResolver(Configuration(repeatCount = { 3 }))

        val result = resolver.resolve(IntArray::class, PrimitiveResolver())

        result as IntArray

        assertEquals(3, result.size)
    }

    @Test
    fun `Length of array matches configuration value of 7`() {
        val resolver = PrimitiveArrayResolver(Configuration(repeatCount = { 7 }))

        val result = resolver.resolve(IntArray::class, PrimitiveResolver())

        result as IntArray

        assertEquals(7, result.size)
    }
}
