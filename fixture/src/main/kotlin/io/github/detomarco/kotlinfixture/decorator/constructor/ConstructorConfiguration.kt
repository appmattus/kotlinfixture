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

package io.github.detomarco.kotlinfixture.decorator.constructor

import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder

/**
 * Choosing the constructor to generate an object with `constructorStrategy`
 *
 * By default, when the library generates an instance of a class it picks a constructor at random. This can be overridden by setting a constructor strategy.
 *
 * ```
 * val fixture = kotlinFixture {
 *     constructorStrategy(ModestConstructorStrategy)
 * }
 * ```
 *
 * #### Available strategies
 *
 * - [RandomConstructorStrategy] order constructors at random.
 * - [ModestConstructorStrategy] order constructors by the most modest constructor first. i.e. fewer parameters returned first.
 * - [GreedyConstructorStrategy] order constructors by the most greedy constructor first. i.e. greater parameters returned first.
 * - [ArrayFavouringConstructorStrategy] order constructors selecting those with the most parameters of `Array<*>` before any other.
 * - [ListFavouringConstructorStrategy] order constructors selecting those with the most parameters of `List<*>` before any other.
 *
 * It is also possible to define and implement your own constructor strategy by implementing [ConstructorStrategy] and applying it as above.
 */
@Suppress("unused")
fun ConfigurationBuilder.constructorStrategy(strategy: ConstructorStrategy) {
    strategies[ConstructorStrategy::class] = strategy
}
