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

import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyTesting
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(Parameterized::class)
class ForAllExtTest {

    @Parameterized.Parameter(0)
    lateinit var testCase: TestCase

    data class Person(val name: String, val age: Int)

    data class TestCase(
        val expectedCount: Int,
        val block: suspend Fixture.(save: (person: List<Person>) -> Unit, returnValue: () -> Boolean) -> Unit
    )

    @Test
    fun `param generates all random values`() {
        runBlocking {
            val generatedValues = mutableSetOf<Person>()

            testCase.block(fixture, { generatedValues.addAll(it) }) { true }

            assertEquals(testCase.expectedCount, generatedValues.size)
        }
    }

    @Test
    fun `param succeeds when all values true`() {
        runBlocking {
            testCase.block(fixture, { }) { true }
        }
    }

    @Test
    fun `param throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                testCase.block(fixture, { }) { false }
            }
        }
    }

    companion object {
        private const val ITERATIONS = 10
        private val fixture = kotlinFixture()

        @JvmStatic
        @Parameterized.Parameters
        @Suppress("LongMethod")
        fun data(): Array<TestCase> = arrayOf(

            // 1 parameter

            TestCase(PropertyTesting.defaultIterationCount) { save, returnValue ->
                forAll<Person> { p1 -> save(listOf(p1)); returnValue() }
            },

            TestCase(ITERATIONS) { save, returnValue ->
                forAll<Person>(ITERATIONS) { p1 -> save(listOf(p1)); returnValue() }
            },

            TestCase(PropertyTesting.defaultIterationCount) { save, returnValue ->
                forAll<Person>(PropTestConfig()) { p1 -> save(listOf(p1)); returnValue() }
            },

            TestCase(ITERATIONS) { save, returnValue ->
                forAll<Person>(ITERATIONS, PropTestConfig()) { p1 -> save(listOf(p1)); returnValue() }
            },

            // 2 parameters

            TestCase(PropertyTesting.defaultIterationCount * 2) { save, returnValue ->
                forAll<Person, Person> { p1, p2 -> save(listOf(p1, p2)); returnValue() }
            },

            TestCase(ITERATIONS * 2) { save, returnValue ->
                forAll<Person, Person>(ITERATIONS) { p1, p2 -> save(listOf(p1, p2)); returnValue() }
            },

            TestCase(PropertyTesting.defaultIterationCount * 2) { save, returnValue ->
                forAll<Person, Person>(PropTestConfig()) { p1, p2 -> save(listOf(p1, p2)); returnValue() }
            },

            TestCase(ITERATIONS * 2) { save, returnValue ->
                forAll<Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2 -> save(listOf(p1, p2)); returnValue() }
            },

            // 3 parameters

            TestCase(PropertyTesting.defaultIterationCount * 3) { save, returnValue ->
                forAll<Person, Person, Person> { p1, p2, p3 -> save(listOf(p1, p2, p3)); returnValue() }
            },

            TestCase(ITERATIONS * 3) { save, returnValue ->
                forAll<Person, Person, Person>(ITERATIONS) { p1, p2, p3 -> save(listOf(p1, p2, p3)); returnValue() }
            },

            TestCase(PropertyTesting.defaultIterationCount * 3) { save, returnValue ->
                forAll<Person, Person, Person>(PropTestConfig()) { p1, p2, p3 ->
                    save(listOf(p1, p2, p3)); returnValue()
                }
            },

            TestCase(ITERATIONS * 3) { save, returnValue ->
                forAll<Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3 ->
                    save(listOf(p1, p2, p3)); returnValue()
                }
            },

            // 4 parameters

            TestCase(PropertyTesting.defaultIterationCount * 4) { save, returnValue ->
                forAll<Person, Person, Person, Person> { p1, p2, p3, p4 -> save(listOf(p1, p2, p3, p4)); returnValue() }
            },

            TestCase(ITERATIONS * 4) { save, returnValue ->
                forAll<Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); returnValue()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 4) { save, returnValue ->
                forAll<Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); returnValue()
                }
            },

            TestCase(ITERATIONS * 4) { save, returnValue ->
                forAll<Person, Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); returnValue()
                }
            },

            // 5 parameters

            TestCase(PropertyTesting.defaultIterationCount * 5) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person> { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); returnValue()
                }
            },

            TestCase(ITERATIONS * 5) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); returnValue()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 5) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); returnValue()
                }
            },

            TestCase(ITERATIONS * 5) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); returnValue()
                }
            },

            // 6 parameters

            TestCase(PropertyTesting.defaultIterationCount * 6) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person, Person> { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); returnValue()
                }
            },

            TestCase(ITERATIONS * 6) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); returnValue()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 6) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); returnValue()
                }
            },

            TestCase(ITERATIONS * 6) { save, returnValue ->
                forAll<Person, Person, Person, Person, Person, Person>(
                    ITERATIONS,
                    PropTestConfig()
                ) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); returnValue()
                }
            }
        )
    }
}
