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

package com.detomarco.kotlinfixture.decorator.logging

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
