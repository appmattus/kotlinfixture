package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KTypeResolverTest {
    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = CompositeResolver(PrimitiveResolver(), KTypeResolver())
    }

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Int kType calls resolver with Int`() {
        val result = context.resolve(Int::class.createType())

        assertNotNull(result)
        assertEquals(Int::class, result::class)
    }

    @Test
    fun `Nullable Int kType randomly returns either null or Int`() {
        assertIsRandom {
            context.resolve(Int::class.createType(nullable = true)) == null
        }
    }
}
