package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UrlResolverTest {
    private val resolver = UrlResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `URL class returns url`() {
        val result = resolver.resolve(URL::class, resolver)

        assertNotNull(result)
        assertEquals(URL::class, result::class)
    }
}
