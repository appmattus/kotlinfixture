package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectResolverTest {
    private val resolver = ObjectResolver()

    object TestObject

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `object class returns instance`() {
        val result = resolver.resolve(TestObject::class, resolver)

        assertEquals(TestObject, result)
    }
}
