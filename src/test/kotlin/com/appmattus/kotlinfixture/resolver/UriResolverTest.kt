package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UriResolverTest {
    private val context = TestContext(Configuration(), CompositeResolver(UriResolver(), UrlResolver()))

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `URI class returns uri`() {
        val result = context.resolve(URI::class)

        assertNotNull(result)
        assertEquals(URI::class, result::class)
    }
}
