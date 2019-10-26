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
import kotlin.test.assertFailsWith

class ForAllExtTest {
    private val fixture = kotlinFixture()

    @Test
    fun `one param succeeds when all values true`() {
        fixture.forAll(10) { _: Person -> true }
    }

    @Test
    fun `one param throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person -> false }
        }
    }

    @Test
    fun `two params succeeds when all values true`() {
        fixture.forAll(10) { _: Person, _: Person -> true }
    }

    @Test
    fun `two params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person, _: Person -> false }
        }
    }

    @Test
    fun `three params succeeds when all values true`() {
        fixture.forAll(10) { _: Person, _: Person, _: Person -> true }
    }

    @Test
    fun `three params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person, _: Person, _: Person -> false }
        }
    }

    @Test
    fun `four params succeeds when all values true`() {
        fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person -> true }
    }

    @Test
    fun `four params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person -> false }
        }
    }

    @Test
    fun `five params succeeds when all values true`() {
        fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person, _: Person -> true }
    }

    @Test
    fun `five params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person, _: Person -> false }
        }
    }

    @Test
    fun `six params succeeds when all values true`() {
        fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> true }
    }

    @Test
    fun `six params throws when all values false`() {
        assertFailsWith<AssertionError> {
            fixture.forAll(10) { _: Person, _: Person, _: Person, _: Person, _: Person, _: Person -> false }
        }
    }

    data class Person(val name: String, val age: Int)
}
