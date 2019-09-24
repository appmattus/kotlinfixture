package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KTypeResolverTest {
    private val resolver = KTypeResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Int kType calls resolver with Int`() {
        val result = resolver.resolve(Int::class.createType(), PrimitiveResolver())

        assertNotNull(result)
        assertEquals(Int::class, result::class)
    }

    @Test
    fun `Nullable Int kType randomly returns either null or Int`() {
        assertIsRandom {
            resolver.resolve(Int::class.createType(nullable = true), PrimitiveResolver()) == null
        }
    }
}
