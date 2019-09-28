package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class PrimitiveArrayResolverTest {
    val context = TestContext(Configuration(), CompositeResolver(PrimitiveArrayResolver(), PrimitiveResolver()))

    class Single : PrimitiveArrayResolverTest() {
        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Length of array matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            val result = context.resolve(IntArray::class) as IntArray

            assertEquals(3, result.size)
        }

        @Test
        fun `Length of array matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            val result = context.resolve(IntArray::class) as IntArray

            assertEquals(7, result.size)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised : PrimitiveArrayResolverTest() {
        @Parameterized.Parameter(0)
        lateinit var clazz: KClass<*>

        @Test
        fun `Returns correct type`() {
            val result = context.resolve(clazz)

            assertNotNull(result)
            assertEquals(clazz, result::class)
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                context.resolve(clazz)
            }
        }
    }

    companion object {
        @JvmStatic
        @Suppress("EXPERIMENTAL_API_USAGE")
        @Parameterized.Parameters(name = "{0}")
        fun data() = arrayOf(
            arrayOf(BooleanArray::class),
            arrayOf(ByteArray::class),
            arrayOf(DoubleArray::class),
            arrayOf(FloatArray::class),
            arrayOf(IntArray::class),
            arrayOf(LongArray::class),
            arrayOf(ShortArray::class),

            arrayOf(UByteArray::class),
            arrayOf(UIntArray::class),
            arrayOf(ULongArray::class),
            arrayOf(UShortArray::class)
        )
    }
}
