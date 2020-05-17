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

package com.appmattus.kotlinfixture.kotest

import com.appmattus.kotlinfixture.kotlinFixture
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ForAllExtTest {
    private val fixture = kotlinFixture()

    @Test
    fun `one param generates all random values`() {
        runBlocking {
            val generatedData = mutableSetOf<Person>()
            fixture.forAll(ITERATIONS) { person: Person ->
                generatedData.add(person)
                true
            }

            assertEquals(ITERATIONS, generatedData.size)
        }
    }

    @Test
    fun `one param succeeds when all values true`() {
        runBlocking {
            fixture.forAll<Person>(ITERATIONS) { true }
        }
    }

    @Test
    fun `one param throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll<Person>(ITERATIONS) { false }
            }
        }
    }

    @Test
    fun `two param generates all random values`() {
        runBlocking {
            val generatedData = mutableSetOf<Person>()
            fixture.forAll(ITERATIONS) { person1: Person, person2: Person ->
                generatedData.add(person1)
                generatedData.add(person2)
                true
            }

            assertEquals(ITERATIONS * 2, generatedData.size)
        }
    }

    @Test
    fun `two params succeeds when all values true`() {
        runBlocking {
            fixture.forAll(ITERATIONS) { _: Person, _: Person -> true }
        }
    }

    @Test
    fun `two params throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll(ITERATIONS) { _: Person, _: Person -> false }
            }
        }
    }

    @Test
    fun `three params succeeds when all values true`() {
        runBlocking {
            fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person -> true }
        }
    }

    @Test
    fun `three params throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person -> false }
            }
        }
    }

    @Test
    fun `four params succeeds when all values true`() {
        runBlocking {
            fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person -> true }
        }
    }

    @Test
    fun `four params throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person -> false }
            }
        }
    }

    @Test
    fun `five params succeeds when all values true`() {
        runBlocking {
            fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person, _: Person -> true }
        }
    }

    @Test
    fun `five params throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person, _: Person -> false }
            }
        }
    }

    @Test
    fun `six params succeeds when all values true`() {
        runBlocking {
            fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> true }
        }
    }

    @Test
    fun `six params throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                fixture.forAll(ITERATIONS) { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> false }
            }
        }
    }

    data class Person(val name: String, val age: Int)

    companion object {
        private const val ITERATIONS = 10
    }
}
