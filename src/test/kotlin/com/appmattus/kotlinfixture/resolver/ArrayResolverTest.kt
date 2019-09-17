package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ArrayResolverTest {
    private val resolver = CompositeResolver(listOf(ArrayResolver(Configuration()), StringResolver(), PrimitiveResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Primitive array class returns Unresolved`() {
        val result = resolver.resolve(IntArray::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Array-Int class returns int class array`() {
        val result = resolver.resolve(Array<Int>::class, resolver)

        assertNotNull(result)
        assertEquals(Array<Int>::class, result::class)
    }

    @Test
    fun `Array-String class returns string array`() {
        val result = resolver.resolve(Array<String>::class, resolver)

        assertNotNull(result)
        assertEquals(Array<String>::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(Array<String>::class, resolver)
        }
    }


    @Test
    fun `Length of array matches configuration value of 3`() {
        val resolver = ArrayResolver(Configuration(repeatCount = { 3 }))

        val result = resolver.resolve(Array<String>::class, StringResolver())

        result as Array<*>

        assertEquals(3, result.size)
    }

    @Test
    fun `Length of array matches configuration value of 7`() {
        val resolver = ArrayResolver(Configuration(repeatCount = { 7 }))

        val result = resolver.resolve(Array<String>::class, StringResolver())

        result as Array<*>

        assertEquals(7, result.size)
    }


    @Test
    fun `Array of arrays`() {
        val resolver = CompositeResolver(listOf(ArrayResolver(Configuration(repeatCount = { 3 })), StringResolver()))

        val result = resolver.resolve(Array<Array<String>>::class, resolver)

        @Suppress("UNCHECKED_CAST")
        result as Array<Array<String>>

        assertEquals(3, result.size)
        result.forEach {
            assertEquals(3, it.size)
        }
    }
}
