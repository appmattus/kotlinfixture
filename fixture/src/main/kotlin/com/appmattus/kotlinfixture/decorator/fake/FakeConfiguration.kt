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

package com.appmattus.kotlinfixture.decorator.fake

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

/**
 * Fake object generation with `fakeStrategy`
 *
 * A faker intercepts the generation of named properties so their values can be replaced with fake data, useful if you
 * need to generate objects with pretty data.
 *
 * See the `fixture-javafaker` module.
 */
fun ConfigurationBuilder.fakeStrategy(strategy: FakeStrategy) {
    strategies[FakeStrategy::class] = strategy
}

/**
 * The current [FakeStrategy] in the  [ConfigurationBuilder] or null if none set.
 * @suppress
 */
val ConfigurationBuilder.fakeStrategy: FakeStrategy?
    get() = strategies[FakeStrategy::class] as? FakeStrategy
