package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BigDecimalResolverTest {
    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = BigDecimalResolver()
    }

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `BigDecimal class returns big decimal`() {
        val result = context.resolve(BigDecimal::class)

        assertNotNull(result)
        assertEquals(BigDecimal::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(BigDecimal::class)
        }
    }
}
