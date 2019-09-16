package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.random.Random
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class KTypeResolverTest {
    private val resolver = KTypeResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Int kType calls resolver with Int`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return if (obj == Int::class) Random.nextInt() else fail()
            }
        }

        val result = resolver.resolve(Int::class.createType(), testResolver)

        assertNotNull(result)
        assertEquals(Int::class, result::class)
    }

    @Test
    fun `Nullable Int kType randomly returns either null or Int`() {
        val testResolver = object : Resolver {
            override fun resolve(obj: Any?, resolver: Resolver): Any? {
                return if (obj == Int::class) Random.nextInt() else fail()
            }
        }

        val allValues = mutableListOf<Int?>()

        repeat(10) {
            resolver.resolve(Int::class.createType(nullable = true), testResolver).apply {
                allValues.add(this as Int?)
            }
        }

        assertTrue { allValues.any { it == null } }
        assertTrue { allValues.any { it != null } }
    }
}
