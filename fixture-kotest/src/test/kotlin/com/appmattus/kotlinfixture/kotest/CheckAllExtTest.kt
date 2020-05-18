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
import kotlin.test.fail

@RunWith(Parameterized::class)
class CheckAllExtTest {

    @Parameterized.Parameter(0)
    lateinit var testCase: TestCase

    data class Person(val name: String, val age: Int)

    data class TestCase(
        val expectedCount: Int,
        val block: suspend Fixture.(save: (person: List<Person>) -> Unit, result: () -> Unit) -> Unit
    )

    @Test
    fun `param generates all random values`() {
        runBlocking {
            val generatedValues = mutableSetOf<Person>()

            testCase.block(fixture, { generatedValues.addAll(it) }) { }

            assertEquals(testCase.expectedCount, generatedValues.size)
        }
    }

    @Test
    fun `param succeeds when all values true`() {
        runBlocking {
            testCase.block(fixture, { }) { }
        }
    }

    @Test
    fun `param throws when all values false`() {
        runBlocking {
            assertFailsWith<AssertionError> {
                testCase.block(fixture, { }) { fail() }
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

            TestCase(PropertyTesting.defaultIterationCount) { save, result ->
                checkAll<Person> { p1 -> save(listOf(p1)); result() }
            },

            TestCase(ITERATIONS) { save, result ->
                checkAll<Person>(ITERATIONS) { p1 -> save(listOf(p1)); result() }
            },

            TestCase(PropertyTesting.defaultIterationCount) { save, result ->
                checkAll<Person>(PropTestConfig()) { p1 -> save(listOf(p1)); result() }
            },

            TestCase(ITERATIONS) { save, result ->
                checkAll<Person>(ITERATIONS, PropTestConfig()) { p1 -> save(listOf(p1)); result() }
            },

            // 2 parameters

            TestCase(PropertyTesting.defaultIterationCount * 2) { save, result ->
                checkAll<Person, Person> { p1, p2 -> save(listOf(p1, p2)); result() }
            },

            TestCase(ITERATIONS * 2) { save, result ->
                checkAll<Person, Person>(ITERATIONS) { p1, p2 -> save(listOf(p1, p2)); result() }
            },

            TestCase(PropertyTesting.defaultIterationCount * 2) { save, result ->
                checkAll<Person, Person>(PropTestConfig()) { p1, p2 -> save(listOf(p1, p2)); result() }
            },

            TestCase(ITERATIONS * 2) { save, result ->
                checkAll<Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2 -> save(listOf(p1, p2)); result() }
            },

            // 3 parameters

            TestCase(PropertyTesting.defaultIterationCount * 3) { save, result ->
                checkAll<Person, Person, Person> { p1, p2, p3 -> save(listOf(p1, p2, p3)); result() }
            },

            TestCase(ITERATIONS * 3) { save, result ->
                checkAll<Person, Person, Person>(ITERATIONS) { p1, p2, p3 -> save(listOf(p1, p2, p3)); result() }
            },

            TestCase(PropertyTesting.defaultIterationCount * 3) { save, result ->
                checkAll<Person, Person, Person>(PropTestConfig()) { p1, p2, p3 ->
                    save(listOf(p1, p2, p3)); result()
                }
            },

            TestCase(ITERATIONS * 3) { save, result ->
                checkAll<Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3 ->
                    save(listOf(p1, p2, p3)); result()
                }
            },

            // 4 parameters

            TestCase(PropertyTesting.defaultIterationCount * 4) { save, result ->
                checkAll<Person, Person, Person, Person> { p1, p2, p3, p4 -> save(listOf(p1, p2, p3, p4)); result() }
            },

            TestCase(ITERATIONS * 4) { save, result ->
                checkAll<Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); result()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 4) { save, result ->
                checkAll<Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); result()
                }
            },

            TestCase(ITERATIONS * 4) { save, result ->
                checkAll<Person, Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3, p4 ->
                    save(listOf(p1, p2, p3, p4)); result()
                }
            },

            // 5 parameters

            TestCase(PropertyTesting.defaultIterationCount * 5) { save, result ->
                checkAll<Person, Person, Person, Person, Person> { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); result()
                }
            },

            TestCase(ITERATIONS * 5) { save, result ->
                checkAll<Person, Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); result()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 5) { save, result ->
                checkAll<Person, Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); result()
                }
            },

            TestCase(ITERATIONS * 5) { save, result ->
                checkAll<Person, Person, Person, Person, Person>(ITERATIONS, PropTestConfig()) { p1, p2, p3, p4, p5 ->
                    save(listOf(p1, p2, p3, p4, p5)); result()
                }
            },

            // 6 parameters

            TestCase(PropertyTesting.defaultIterationCount * 6) { save, result ->
                checkAll<Person, Person, Person, Person, Person, Person> { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); result()
                }
            },

            TestCase(ITERATIONS * 6) { save, result ->
                checkAll<Person, Person, Person, Person, Person, Person>(ITERATIONS) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); result()
                }
            },

            TestCase(PropertyTesting.defaultIterationCount * 6) { save, result ->
                checkAll<Person, Person, Person, Person, Person, Person>(PropTestConfig()) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); result()
                }
            },

            TestCase(ITERATIONS * 6) { save, result ->
                checkAll<Person, Person, Person, Person, Person, Person>(
                    ITERATIONS,
                    PropTestConfig()
                ) { p1, p2, p3, p4, p5, p6 ->
                    save(listOf(p1, p2, p3, p4, p5, p6)); result()
                }
            }
        )
    }
}
