package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BigIntegerResolverTest {
    private val context = TestContext(Configuration(), BigIntegerResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BigInteger class returns big int`() {
        val result = context.resolve(BigInteger::class)

        assertNotNull(result)
        assertEquals(BigInteger::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(BigInteger::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(BigInteger::class) as BigInteger
        val value2 = context.seedRandom().resolve(BigInteger::class) as BigInteger

        assertEquals(value1, value2)
    }
}
