/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.decorator.config

import com.detomarco.kotlinfixture.assertIsRandom
import com.detomarco.kotlinfixture.config.regexToRandom
import com.detomarco.kotlinfixture.kotlinFixture
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorStringKtTest {

    private val pattern = "[0-9]-[A-Z]{0,3}"
    private val regex = pattern.toRegex()

    @Test
    fun `regexToRandom using regex generates string matching regex`() {

        val fixture = kotlinFixture {
            factory<String> { regexToRandom(regex) }
        }

        repeat(100) {
            val result = fixture<String>()

            assertTrue {
                result.matches(regex)
            }
        }
    }

    @Test
    fun `regexToRandom using regex is random`() {

        val fixture = kotlinFixture {
            factory<String> { regexToRandom(regex) }
        }

        assertIsRandom {
            fixture<String>()
        }
    }

    @Test
    fun `regexToRandom using regex uses seeded random`() {
        val fixture = kotlinFixture {
            factory<String> { regexToRandom(regex) }
        }

        val value1 = fixture<String> {
            random = Random(0)
        }
        val value2 = fixture<String> {
            random = Random(0)
        }

        assertEquals(value1, value2)
    }

    @Test
    fun `regexToRandom using string generates string matching regex`() {

        val fixture = kotlinFixture {
            factory<String> { regexToRandom(pattern) }
        }

        repeat(100) {
            val result = fixture<String>()

            assertTrue {
                result.matches(regex)
            }
        }
    }

    @Test
    fun `regexToRandom using string is random`() {

        val fixture = kotlinFixture {
            factory<String> { regexToRandom(pattern) }
        }

        assertIsRandom {
            fixture<String>()
        }
    }

    @Test
    fun `regexToRandom using string uses seeded random`() {
        val fixture = kotlinFixture {
            factory<String> { regexToRandom(pattern) }
        }

        val value1 = fixture<String> {
            random = Random(0)
        }
        val value2 = fixture<String> {
            random = Random(0)
        }

        assertEquals(value1, value2)
    }
}
