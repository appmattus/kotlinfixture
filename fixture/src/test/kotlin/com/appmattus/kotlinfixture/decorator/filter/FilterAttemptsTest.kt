/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture.decorator.filter

import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FilterAttemptsTest {
    @Test(timeout = 5_000)
    fun `impossible predicate stops at the configured limit`() {
        var generated = 0
        val fixture = kotlinFixture {
            maxFilterAttempts = 3
            factory<Int> { ++generated }
            filter<Int> { filter { false } }
        }

        val failure = assertFailsWith<UnsupportedOperationException> { fixture<Int>() }

        assertEquals(3, generated)
        assertTrue(failure.message.orEmpty().contains("kotlin.Int"))
        assertTrue(failure.message.orEmpty().contains("maxFilterAttempts=3"))
    }

    @Test(timeout = 5_000)
    fun `exhausted distinct filter fails instead of hanging`() {
        var generated = 0
        val fixture = kotlinFixture {
            maxFilterAttempts = 3
            factory<Boolean> {
                generated++
                true
            }
            filter<Boolean> { distinct() }
        }

        assertTrue(fixture<Boolean>())
        assertFailsWith<UnsupportedOperationException> { fixture<Boolean>() }
        assertEquals(4, generated)
    }

    @Test(timeout = 5_000)
    fun `chained filters share a budget which resets after each output`() {
        var generated = 0
        val fixture = kotlinFixture {
            maxFilterAttempts = 6
            factory<Int> { ++generated }
            filter<Int> { filter { it % 2 == 0 } }
            filter<Int> { filter { it % 3 == 0 } }
        }

        assertEquals(6, fixture<Int>())
        assertEquals(12, fixture<Int>())
    }

    @Test(timeout = 5_000)
    fun `per call limit overrides inherited configuration`() {
        var generated = 0
        val fixture = kotlinFixture {
            maxFilterAttempts = 2
            factory<Int> { ++generated }
            filter<Int> { filter { it % 3 == 0 } }
        }.new { }

        assertEquals(3, fixture<Int> { maxFilterAttempts = 3 })
        assertFailsWith<UnsupportedOperationException> { fixture<Int>() }
        assertEquals(5, generated)
    }

    @Test(timeout = 5_000)
    fun `default budget bounds impossible predicates`() {
        var generated = 0
        val fixture = kotlinFixture {
            factory<Int> { ++generated }
            filter<Int> { filter { false } }
        }

        assertFailsWith<UnsupportedOperationException> { fixture<Int>() }
        assertEquals(1_000, generated)
    }

    @Test
    fun `attempt limit must be positive`() {
        assertFailsWith<IllegalArgumentException> { kotlinFixture { maxFilterAttempts = 0 } }
        assertFailsWith<IllegalArgumentException> { kotlinFixture { maxFilterAttempts = -1 } }
        assertEquals(1_000, ConfigurationBuilder().maxFilterAttempts)
    }
}
