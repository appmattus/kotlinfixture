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

package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.Fixture
import kotlin.random.Random

/**
 * Generators for `factory` and `property` configuration
 * @property random The fixtures random, which may be seeded
 * @property fixture A fixture to be able to generate nested objects
 */
interface Generator<T> {
    val random: Random
    val fixture: Fixture
}

internal typealias GeneratorFun = Generator<Any?>.() -> Any?

/**
 * Generating values in a [range]
 *
 * `range` function to make it easy to generate values in a range.
 *
 * ```
 * factory<Int> { range(1..10) }
 * ```
 */
fun <T> Generator<T>.range(range: Iterable<T>) =
    range.shuffled(random).firstOrNull() ?: throw NoSuchElementException("Range is empty")
