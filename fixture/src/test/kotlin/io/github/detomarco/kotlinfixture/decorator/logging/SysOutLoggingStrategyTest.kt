/*
 * Copyright 2019 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.detomarco.kotlinfixture.decorator.logging

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class SysOutLoggingStrategyTest {
    @Test
    fun `single request logs value`() {
        val value = UUID.randomUUID().toString()

        val output = captureSysOut {
            SysOutLoggingStrategy.request(value)
        }

        assertTrue {
            output.contains(value)
        }
    }

    @Test
    fun `multiple requests log both values`() {
        val value1 = UUID.randomUUID().toString()
        val value2 = UUID.randomUUID().toString()

        val output = captureSysOut {
            SysOutLoggingStrategy.request(value1)
            SysOutLoggingStrategy.request(value2)
        }

        assertTrue { output.contains(value1) }
        assertTrue { output.contains(value2) }
    }

    @Test
    fun `first nested request is indented`() {
        val value1 = UUID.randomUUID().toString()
        val value2 = UUID.randomUUID().toString()

        val output = captureSysOut {
            SysOutLoggingStrategy.request(value1)
            SysOutLoggingStrategy.request(value2)
        }

        val indent1 = output.lines().first { it.contains(value1) }.indexOf(value1)
        val indent2 = output.lines().first { it.contains(value2) }.indexOf(value2)

        assertEquals(indent1 + 4, indent2)
    }

    @Test
    fun `nested requests are indented`() {
        val value1 = UUID.randomUUID().toString()
        val value2 = UUID.randomUUID().toString()
        val value3 = UUID.randomUUID().toString()

        val output = captureSysOut {
            SysOutLoggingStrategy.request(value1)
            SysOutLoggingStrategy.request(value2)
            SysOutLoggingStrategy.response(value2, Result.success(Unit))
            SysOutLoggingStrategy.request(value3)
        }

        val indent1 = output.lines().first { it.contains(value1) }.indexOf(value1)
        val indent3 = output.lines().first { it.contains(value3) }.indexOf(value3)

        assertEquals(indent1 + 4, indent3)
    }

    @Test
    fun `single request logs response`() {
        val value = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        val output = captureSysOut {
            SysOutLoggingStrategy.request(value)
            SysOutLoggingStrategy.response(value, Result.success(response))
        }

        assertTrue {
            output.contains("Success($response)")
        }
    }

    @Test
    fun `nested responses are indented`() {
        val value1 = UUID.randomUUID().toString()
        val value2 = UUID.randomUUID().toString()
        val response1 = UUID.randomUUID().toString()
        val response2 = UUID.randomUUID().toString()

        val output = captureSysOut {
            with(SysOutLoggingStrategy) {
                request(value1)
                request(value2)
                response(value2, Result.success(response2))
                response(value1, Result.success(response1))
            }
        }

        val indent1 = output.lines().first { it.contains(response1) }.indexOf(response1)
        val indent2 = output.lines().first { it.contains(response2) }.indexOf(response2)

        assertEquals(indent1 + 4, indent2)
    }
}
