package com.appmattus.kotlinfixture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FixtureInvokeTypeTest {

    val fixture = kotlinFixture()

    @Test
    fun `can create int`() {
        val int = fixture<Int>()
        assertEquals(Int::class, int::class)
    }

    @Test
    fun `can create nullable int`() {
        assertIsRandom {
            fixture<Int?>() == null
        }
    }

    @Test
    fun `can create double`() {
        val double = fixture<Double>()
        assertEquals(Double::class, double::class)
    }

    @Test
    fun `can create list of Strings`() {
        val list = fixture<List<String>>()

        assertTrue(List::class.isInstance(list))

        list.forEach {
            assertEquals(String::class, it::class)
        }
    }

    @Test
    fun `can create array of Strings`() {
        val array = fixture<Array<String>>()

        assertTrue(Array<String>::class.isInstance(array))

        array.forEach {
            assertEquals(String::class, it::class)
        }
    }
}
