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

package io.github.detomarco.kotlinfixture

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

    @Test
    fun `can create Set`() {
        val set = fixture<Set<A>>()

        assertTrue(Set::class.isInstance(set))

        set.forEach {
            assertEquals(A::class, it::class)
        }
    }

    data class A(
        val string: String
    )
}
