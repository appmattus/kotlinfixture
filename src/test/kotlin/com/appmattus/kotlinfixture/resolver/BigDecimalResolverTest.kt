package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BigDecimalResolverTest {
    private val resolver = BigDecimalResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BigDecimal class returns big decimal`() {
        val result = resolver.resolve(BigDecimal::class, resolver)

        assertNotNull(result)
        assertEquals(BigDecimal::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(BigDecimal::class, resolver)
        }
    }
}
