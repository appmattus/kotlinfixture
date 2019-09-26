package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PrimitiveArrayResolverTest {
    private val context = TestContext(Configuration(), CompositeResolver(PrimitiveArrayResolver(), PrimitiveResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BooleanArray class returns boolean array`() {
        val result = context.resolve(BooleanArray::class)

        assertNotNull(result)
        assertEquals(BooleanArray::class, result::class)
    }

    @Test
    fun `Random BooleanArray values returned`() {
        assertIsRandom {
            context.resolve(BooleanArray::class)
        }
    }

    @Test
    fun `ByteArray class returns byte array`() {
        val result = context.resolve(ByteArray::class)

        assertNotNull(result)
        assertEquals(ByteArray::class, result::class)
    }

    @Test
    fun `Random ByteArray values returned`() {
        assertIsRandom {
            context.resolve(ByteArray::class)
        }
    }

    @Test
    fun `DoubleArray class returns double array`() {
        val result = context.resolve(DoubleArray::class)

        assertNotNull(result)
        assertEquals(DoubleArray::class, result::class)
    }

    @Test
    fun `Random DoubleArray values returned`() {
        assertIsRandom {
            context.resolve(DoubleArray::class)
        }
    }

    @Test
    fun `FloatArray class returns float array`() {
        val result = context.resolve(FloatArray::class)

        assertNotNull(result)
        assertEquals(FloatArray::class, result::class)
    }

    @Test
    fun `Random FloatArray values returned`() {
        assertIsRandom {
            context.resolve(FloatArray::class)
        }
    }

    @Test
    fun `IntArray class returns int array`() {
        val result = context.resolve(IntArray::class)

        assertNotNull(result)
        assertEquals(IntArray::class, result::class)
    }

    @Test
    fun `Random IntArray values returned`() {
        assertIsRandom {
            context.resolve(IntArray::class)
        }
    }

    @Test
    fun `LongArray class returns long array`() {
        val result = context.resolve(LongArray::class)

        assertNotNull(result)
        assertEquals(LongArray::class, result::class)
    }

    @Test
    fun `Random LongArray values returned`() {
        assertIsRandom {
            context.resolve(LongArray::class)
        }
    }

    @Test
    fun `ShortArray class returns short array`() {
        val result = context.resolve(ShortArray::class)

        assertNotNull(result)
        assertEquals(ShortArray::class, result::class)
    }

    @Test
    fun `Random ShortArray values returned`() {
        assertIsRandom {
            context.resolve(ShortArray::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UByteArray class returns ubyte array`() {
        val result = context.resolve(UByteArray::class)

        assertNotNull(result)
        assertEquals(UByteArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UByteArray values returned`() {
        assertIsRandom {
            context.resolve(UByteArray::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UIntArray class returns unit array`() {
        val result = context.resolve(UIntArray::class)

        assertNotNull(result)
        assertEquals(UIntArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UIntArray values returned`() {
        assertIsRandom {
            context.resolve(UIntArray::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `ULongArray class returns ulong array`() {
        val result = context.resolve(ULongArray::class)

        assertNotNull(result)
        assertEquals(ULongArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random ULongArray values returned`() {
        assertIsRandom {
            context.resolve(ULongArray::class)
        }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `UShortArray class returns ushort array`() {
        val result = context.resolve(UShortArray::class)

        assertNotNull(result)
        assertEquals(UShortArray::class, result::class)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE")
    fun `Random UShortArray values returned`() {
        assertIsRandom {
            context.resolve(UShortArray::class)
        }
    }

    @Test
    fun `Length of array matches configuration value of 3`() {
        val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

        val result = context.resolve(IntArray::class)

        result as IntArray

        assertEquals(3, result.size)
    }

    @Test
    fun `Length of array matches configuration value of 7`() {
        val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

        val result = context.resolve(IntArray::class)

        result as IntArray

        assertEquals(7, result.size)
    }
}
