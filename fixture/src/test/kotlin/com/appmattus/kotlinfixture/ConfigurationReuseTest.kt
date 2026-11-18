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

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

/**
 * Without a configuration block, a fixture generates values with its own configuration rather than an identical copy
 */
class ConfigurationReuseTest {

    data class Person(val name: String, val age: Int)

    @Test
    fun `no configuration block uses the fixture's configuration`() {
        val fixture = kotlinFixture()

        assertSame(fixture.fixtureConfiguration, fixture.configurationWith(noConfiguration))
    }

    @Test
    fun `a configuration block builds a new configuration`() {
        val fixture = kotlinFixture()

        assertNotSame(fixture.fixtureConfiguration, fixture.configurationWith { })
    }

    @Test
    fun `seeded values are the same with and without a configuration block`() {
        val withoutBlock = kotlinFixture { random = Random(SEED) }
        val withEmptyBlock = kotlinFixture { random = Random(SEED) }

        val expected = List(SAMPLES) { withEmptyBlock<Person> { } }

        assertEquals(expected, List(SAMPLES) { withoutBlock<Person>() })
    }

    @Test
    fun `fixture configuration applies without a configuration block`() {
        val fixture = kotlinFixture {
            factory<String> { "configured" }
        }

        assertEquals("configured", fixture<String>())
        assertEquals("configured", fixture.asSequence<String>().first())
    }

    @Test
    fun `configuration block applies to that value only`() {
        val fixture = kotlinFixture()

        assertEquals("block", fixture<String> { factory<String> { "block" } })
        assertNotEquals("block", fixture<String>())
    }

    private companion object {
        private const val SEED = 42
        private const val SAMPLES = 20
    }
}
