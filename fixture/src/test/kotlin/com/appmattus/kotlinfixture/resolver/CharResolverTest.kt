package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CharResolverTest {
    private val context = TestContext(Configuration(), CharResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Char class returns char`() {
        val result = context.resolve(Char::class)

        assertNotNull(result)
        assertEquals(Char::class, result::class)
    }

    @Test
    fun `Char class returns lowercase char`() {
        repeat(100) {
            val result = context.resolve(Char::class)

            assertTrue {
                result in 'a'..'z'
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Char::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(Char::class) as Char
        val value2 = context.seedRandom().resolve(Char::class) as Char

        assertEquals(value1, value2)
    }
}
