package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UuidResolverTest {
    private val resolver = UuidResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `UUID class returns uuid`() {
        val result = resolver.resolve(UUID::class, resolver)

        assertNotNull(result)
        assertEquals(UUID::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(UUID::class, resolver)
        }
    }
}
