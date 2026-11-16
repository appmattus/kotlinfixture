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

import java.time.Month
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A seeded random must make every generated value reproducible, including choices between enum values, sealed and
 * abstract subclasses, factory methods and the values of a range.
 */
class SeededRandomTest {

    enum class Colour { Red, Orange, Yellow, Green, Blue, Indigo, Violet }

    sealed class Shape {
        data class Circle(val radius: Int) : Shape()
        data class Square(val side: Int) : Shape()
        data class Triangle(val base: Int) : Shape()
        object Point : Shape()
    }

    interface Animal

    class Cat : Animal

    class Dog : Animal

    class Horse : Animal

    // The constructor rejects generated strings, so instances can only be created through the factory methods
    class Created private constructor(val source: String) {
        init {
            require(source in setOf("first", "second", "third", "fourth"))
        }

        companion object {
            fun first() = Created("first")
            fun second() = Created("second")
            fun third() = Created("third")
            fun fourth() = Created("fourth")
        }
    }

    private fun seededFixture() = kotlinFixture { random = Random(SEED) }

    private fun <T> assertReproducible(generate: (Fixture) -> T) {
        val first = seededFixture().let { fixture -> List(SAMPLES) { generate(fixture) } }
        val second = seededFixture().let { fixture -> List(SAMPLES) { generate(fixture) } }

        assertEquals(first, second)
    }

    @Test
    fun `enum values are reproducible`() = assertReproducible { it<Colour>() }

    @Test
    fun `java time Month is reproducible`() = assertReproducible { it<Month>() }

    @Test
    fun `sealed class subclasses are reproducible`() = assertReproducible { it<Shape>()::class }

    @Test
    fun `abstract class implementations are reproducible`() = assertReproducible { it<Animal>()::class }

    @Test
    fun `factory methods are reproducible`() = assertReproducible { it<Created>().source }

    @Test
    fun `range values are reproducible`() = assertReproducible { it(1..100) }

    @Test
    fun `enum with per-call seeded random always returns the same value`() {
        val fixture = kotlinFixture()

        val values = List(SAMPLES) { fixture<Colour> { random = Random(SEED) } }

        assertEquals(1, values.toSet().size, "Got $values")
    }

    @Test
    fun `range with per-call seeded random always returns the same value`() {
        val fixture = kotlinFixture()

        val values = List(SAMPLES) { fixture(1..100) { random = Random(SEED) } }

        assertEquals(1, values.toSet().size, "Got $values")
    }

    private companion object {
        private const val SEED = 42
        private const val SAMPLES = 20
    }
}
