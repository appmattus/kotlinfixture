package com.appmattus.kotlinfixture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeExtTest {
    @Test
    fun `non nullable types convert correctly`() {
        val type = typeOf<List<String>>()

        assertFalse { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable argument convert correctly`() {
        val type = typeOf<List<String?>>()

        assertFalse { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable list convert correctly`() {
        val type = typeOf<List<String>?>()

        assertTrue { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable list and argument convert correctly`() {
        val type = typeOf<List<String?>?>()

        assertTrue { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }
}
