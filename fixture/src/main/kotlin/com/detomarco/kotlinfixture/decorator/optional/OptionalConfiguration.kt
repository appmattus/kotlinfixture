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

package com.detomarco.kotlinfixture.decorator.optional

import com.detomarco.kotlinfixture.config.ConfigurationBuilder

/**
 * Overriding the use of default values with optionalStrategy
 *
 * By default, when the library comes across an optional type, such as `value: String = "default"` it will randomly return the default value, or a generated value. This can be overridden by setting an optional strategy.
 *
 * ```
 * val fixture = kotlinFixture {
 *     // All optionals will be populated with their default value
 *     optionalStrategy(AlwaysOptionalStrategy) {
 *         // You can override the strategy for a particular class
 *         classOverride<AnotherObject>(NeverOptionalStrategy)
 *
 *         // You can override the strategy for a property of a class
 *         propertyOverride(AnotherObject::property, RandomlyOptionalStrategy)
 *     }
 * }
 * ```
 *
 * #### Available strategies
 *
 * - [AlwaysOptionalStrategy] always use the properties default value.
 * - [NeverOptionalStrategy] never use the properties default value.
 * - [RandomlyOptionalStrategy] randomly use the properties default value.
 *
 * It is also possible to define and implement your own optional strategy by implementing [OptionalStrategy] and applying it as above.
 */
@Suppress("unused")
fun ConfigurationBuilder.optionalStrategy(
    defaultStrategy: OptionalStrategy = RandomlyOptionalStrategy,
    init: OptionalStrategyBuilder.() -> Unit = {}
) {
    strategies[OptionalStrategy::class] = OptionalStrategyBuilder(defaultStrategy).apply(init).build()
}
