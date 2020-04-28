/*
 * Copyright 2020 Appmattus Limited
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

import org.junit.Test
import kotlin.test.assertEquals

class FixtureAsSequenceTest {

    @Test
    fun `generates sequence of random values`() {
        val iterator = kotlinFixture().asSequence<Int>().iterator()
        assertIsRandom {
            iterator.next()
        }
    }

    @Test
    fun `configuration can be overridden in sequence creation`() {
        val fixture = kotlinFixture()

        val list = fixture.asSequence<List<String>> {
            repeatCount { 2 }
        }
        assertEquals(2, list.first().size)
    }

    @Test
    fun `configuration can be overridden in sequence creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount { 1 }
        }

        val list = fixture.asSequence<List<String>> {
            repeatCount { 2 }
        }
        assertEquals(2, list.first().size)
    }
}
