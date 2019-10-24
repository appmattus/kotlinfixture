package com.appmattus.kotlinfixture.decorator.logging

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFalse

class NoLoggingStrategyTest {

    @Test
    fun `values and responses are not logged`() {
        val value1 = UUID.randomUUID().toString()
        val value2 = UUID.randomUUID().toString()
        val response1 = UUID.randomUUID().toString()
        val response2 = UUID.randomUUID().toString()

        val output = captureSysOut {
            with(NoLoggingStrategy) {
                request(value1)
                request(value2)
                response(value2, Result.success(response2))
                response(value1, Result.success(response1))
            }
        }

        assertFalse { output.contains(value1) }
        assertFalse { output.contains(value2) }
        assertFalse { output.contains(response1) }
        assertFalse { output.contains(response2) }
    }

}