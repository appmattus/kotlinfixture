package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CharResolverTest {
    private val resolver = CharResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Char class returns char`() {
        val result = resolver.resolve(Char::class, resolver)

        assertNotNull(result)
        assertEquals(Char::class, result::class)
    }

    @Test
    fun `Char class returns lowercase char`() {
        repeat(100) {
            val result = resolver.resolve(Char::class, resolver)

            assertTrue {
                result in 'a'..'z'
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(Char::class, resolver)
        }
    }
}
