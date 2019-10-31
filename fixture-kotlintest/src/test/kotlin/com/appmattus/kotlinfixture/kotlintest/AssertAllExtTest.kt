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

package com.appmattus.kotlinfixture.kotlintest

import com.appmattus.kotlinfixture.kotlinFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class AssertAllExtTest {
    private val fixture = kotlinFixture()

    @Test
    fun `one param generates all random values`() {
        val generatedData = mutableSetOf<Person>()
        fixture.assertAll { person: Person ->
            generatedData.add(person)
        }

        assertEquals(ITERATIONS, generatedData.size)
    }

    @Test
    fun `one param succeeds when all values true`() {
        fixture.assertAll<Person> { }
    }

    @Test
    fun `one param throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll<Person> { fail() }
        }
    }

    @Test
    fun `two param generates all random values`() {
        val generatedData = mutableSetOf<Person>()
        fixture.assertAll { person1: Person, person2: Person ->
            generatedData.add(person1)
            generatedData.add(person2)
        }

        assertEquals(ITERATIONS * 2, generatedData.size)
    }

    @Test
    fun `two params succeeds when all values true`() {
        fixture.assertAll { _: Person, _: Person -> }
    }

    @Test
    fun `two params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll { _: Person, _: Person -> fail() }
        }
    }

    @Test
    fun `three params succeeds when all values true`() {
        fixture.assertAll { _: Person, _: Person, _: Person -> }
    }

    @Test
    fun `three params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll { _: Person, _: Person, _: Person -> fail() }
        }
    }

    @Test
    fun `four params succeeds when all values true`() {
        fixture.assertAll { _: Person, _: Person, _: Person, _: Person -> }
    }

    @Test
    fun `four params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll { _: Person, _: Person, _: Person, _: Person -> fail() }
        }
    }

    @Test
    fun `five params succeeds when all values true`() {
        fixture.assertAll { _: Person, _: Person, _: Person, _: Person, _: Person -> }
    }

    @Test
    fun `five params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll { _: Person, _: Person, _: Person, _: Person, _: Person -> fail() }
        }
    }

    @Test
    fun `six params succeeds when all values true`() {
        fixture.assertAll { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> }
    }

    @Test
    fun `six params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.assertAll { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> fail() }
        }
    }

    data class Person(val name: String, val age: Int)

    companion object {
        private const val ITERATIONS = 1000
    }
}
