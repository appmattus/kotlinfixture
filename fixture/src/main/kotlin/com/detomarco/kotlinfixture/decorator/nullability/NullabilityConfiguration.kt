/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.decorator.nullability

import com.detomarco.kotlinfixture.config.ConfigurationBuilder

/**
 * Overriding nullability with nullabilityStrategy
 *
 * By default, when the library comes across a nullable type, such as `String?` it will randomly return a value or null. This can be overridden by setting a nullability strategy.
 *
 * ```
 * val fixture = kotlinFixture {
 *     // All nullable types will be populated with a value
 *     nullabilityStrategy(NeverNullStrategy)
 * }
 * ```
 *
 * #### Available strategies
 *
 * - [NeverNullStrategy] populate nullable values with a non-null value.
 * - [AlwaysNullStrategy] populate nullable values with `null`.
 * - [RandomlyNullStrategy] populate nullable values randomly with null.
 *
 * It is also possible to define and implement your own nullability strategy by implementing [NullabilityStrategy] and applying it as above.
 */
@Suppress("unused")
fun ConfigurationBuilder.nullabilityStrategy(strategy: NullabilityStrategy) {
    strategies[NullabilityStrategy::class] = strategy
}
