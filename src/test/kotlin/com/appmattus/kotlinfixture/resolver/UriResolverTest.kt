package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UriResolverTest {
    private val resolver = UriResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `URI class returns uri`() {
        val result = resolver.resolve(URI::class, UrlResolver())

        println(result)

        assertNotNull(result)
        assertEquals(URI::class, result::class)
    }
}
