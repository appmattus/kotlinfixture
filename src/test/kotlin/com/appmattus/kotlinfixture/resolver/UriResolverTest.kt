package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UriResolverTest {
    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = CompositeResolver(UriResolver(), UrlResolver())
    }

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `URI class returns uri`() {
        val result = context.resolve(URI::class)

        println(result)

        assertNotNull(result)
        assertEquals(URI::class, result::class)
    }
}
