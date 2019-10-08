package com.appmattus.kotlinfixture

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RandomExtKtTest {
    @Test
    fun `uuid is version 4 of type IETF`() {
        repeat(100) {
            val uuid = Random.nextUuid().toString()

            assertEquals('4', uuid[14])
            assertTrue(uuid[19] in listOf('8', '9', 'a', 'b'))
        }
    }

    @Test
    fun `generates random UUIDs`() {
        assertIsRandom {
            Random.nextUuid().toString()
        }
    }
}
