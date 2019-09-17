package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BigIntegerResolverTest {
    private val resolver = BigIntegerResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BigInteger class returns big int`() {
        val result = resolver.resolve(BigInteger::class, resolver)

        assertNotNull(result)
        assertEquals(BigInteger::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(BigInteger::class, resolver)
        }
    }
}
