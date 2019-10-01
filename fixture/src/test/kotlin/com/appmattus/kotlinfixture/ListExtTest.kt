package com.appmattus.kotlinfixture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListExtTest {
    @Test
    fun `circular iterator loops`() {
        val list = listOf("A", "B", "C").circularIterator().asSequence().take(6).toList()

        assertEquals(list.take(3), list.takeLast(3))
    }

    @Test
    fun `empty list has no next`() {
        val result = emptyList<String>().circularIterator().hasNext()

        assertFalse(result)
    }

    @Test
    fun `empty list throws exception when getting next value`() {
        assertFailsWith(NoSuchElementException::class) {
            emptyList<String>().circularIterator().next()
        }
    }

    @Test
    fun `list with content has no next`() {
        val result = listOf("A").circularIterator().hasNext()

        assertTrue(result)
    }

}
