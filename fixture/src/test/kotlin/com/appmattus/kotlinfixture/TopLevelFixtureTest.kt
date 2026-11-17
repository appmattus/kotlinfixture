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

package com.appmattus.kotlinfixture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * The top-level [fixture] function, see https://github.com/appmattus/kotlinfixture/issues/98
 */
class TopLevelFixtureTest {

    data class Foo(val bar1: String, val bar2: Int, val bar3: List<String>, val bar4: String?)

    @Test
    fun `creates an instance of a class`() {
        val foo = fixture<Foo>().copy(bar2 = -1)

        assertEquals(-1, foo.bar2)
    }

    @Test
    fun `creates random values`() {
        assertIsRandom {
            fixture<Foo>()
        }
    }

    @Test
    fun `picks a value from a range`() {
        val values = listOf("Cat", "Dog", "Horse")

        repeat(SAMPLES) {
            assertTrue(fixture(values) in values)
        }
    }

    @Test
    fun `configuration applies to that value only`() {
        val configured = fixture<String> {
            factory<String> { "configured" }
        }

        assertEquals("configured", configured)
        assertNotEquals("configured", fixture<String>())
    }

    @Test
    fun `local variable called fixture takes precedence`() {
        val fixture = kotlinFixture {
            factory<String> { "local" }
        }

        assertEquals("local", fixture<String>())
    }

    class PropertyPrecedence {
        private val fixture = kotlinFixture {
            factory<String> { "member" }
        }

        @Test
        fun `property called fixture takes precedence`() {
            assertEquals("member", fixture<String>())
        }
    }

    private companion object {
        private const val SAMPLES = 100
    }
}
