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

package com.appmattus.kotlinfixture.kotest

import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.common.ExperimentalKotest
import io.kotest.property.PropTestConfig
import io.kotest.property.Shrinker
import io.kotest.property.checkAll
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalKotest::class)
class KotestGeneratorTest {
    data class Person(val name: String, val ages: List<Int>)

    @Test
    fun `same Kotest seed reproduces nested values and multiple parameters`() = runBlocking {
        suspend fun generate(seed: Long): List<Pair<Person, Int>> {
            val values = mutableListOf<Pair<Person, Int>>()
            kotlinFixture().checkAll<Person, Int>(20, PropTestConfig(seed = seed)) { person, number ->
                values += person to number
            }
            return values
        }

        assertEquals(generate(42), generate(42))
        assertNotEquals(generate(42), generate(43))
    }

    @Test
    fun `replaying the failure seed reproduces inputs through the failing example`() = runBlocking {
        suspend fun failingRun(): Pair<List<Person>, AssertionError> {
            val values = mutableListOf<Person>()
            val failure = assertFailsWith<AssertionError> {
                kotlinFixture().checkAll<Person>(20, PropTestConfig(seed = 42L)) { person ->
                    values += person
                    if (values.size == 3) fail("Reproducible failure")
                }
            }
            return values to failure
        }

        val (first, failure) = failingRun()
        val (replayed, _) = failingRun()
        assertEquals(3, first.size)
        assertEquals(first, replayed)
        assertTrue(failure.message.orEmpty().contains("42"))
    }

    @Test
    fun `forAll uses the same seeded generation`() = runBlocking {
        suspend fun generate(): List<Person> {
            val values = mutableListOf<Person>()
            kotlinFixture().forAll<Person>(20, PropTestConfig(seed = 42L)) { person ->
                values += person
                true
            }
            return values
        }

        assertEquals(generate(), generate())
    }

    @Test
    fun `fixture customisations survive and its random is not consumed`() = runBlocking {
        fun fixture(seed: Int): Fixture = kotlinFixture {
            random = Random(seed)
            repeatCount { 2 }
            factory<Int> { random.nextInt(10, 20) }
            property(Person::name) { "Configured" }
        }
        suspend fun generate(fixture: Fixture): List<Person> {
            val values = mutableListOf<Person>()
            fixture.checkAll<Person>(20, PropTestConfig(seed = 42L)) { person ->
                values += person
                assertEquals("Configured", person.name)
                assertEquals(2, person.ages.size)
                assertTrue(person.ages.all { it in 10 until 20 })
            }
            return values
        }

        val firstFixture = fixture(1)
        assertEquals(generate(firstFixture), generate(fixture(2)))
        assertEquals(Random(1).nextInt(), firstFixture.fixtureConfiguration.random.nextInt())
    }

    @Test
    fun `custom shrinker reduces a failing example`() = runBlocking {
        val values = mutableListOf<Int>()
        val fixture = kotlinFixture { factory<Int> { 100 } }
        val arb = fixture.kotestGen(Shrinker<Int> { if (it == 0) emptyList() else listOf(0) })

        assertFailsWith<AssertionError> {
            checkAll(PropTestConfig(seed = 42L), arb) { value ->
                values += value
                fail("Always fails")
            }
        }

        assertEquals(100, values.first())
        assertTrue(0 in values)
    }
}
