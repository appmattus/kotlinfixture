package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectResolverTest {
    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = ObjectResolver()
    }

    object TestObject

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `object class returns instance`() {
        val result = context.resolve(TestObject::class)

        assertEquals(TestObject, result)
    }
}
