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

package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

/**
 * Changing how recursion behaves with `recursionStrategy`
 *
 * When the library detects recursion, by default, it will throw an [UnsupportedOperationException] with the details of the circular reference. This strategy can be changed to instead return `null` for the reference, however, if this results in an invalid object an exception will still be thrown as the object requested couldn’t be resolved.
 *
 * ```
 * val fixture = kotlinFixture {
 *     recursionStrategy(NullRecursionStrategy)
 * }
 * ```
 *
 * #### Available strategies
 *
 * - [NullRecursionStrategy] use null for circular references.
 * - [ThrowingRecursionStrategy] throw an exception when finding circular references.
 * - [UnresolvedRecursionStrategy] use Unresolved for circular references, which may result in generation of a valid object as other scenarios will be tried
 *
 * It is also possible to define and implement your own recursion strategy by implementing [RecursionStrategy] and applying it as above.
 */
@Suppress("unused")
fun ConfigurationBuilder.recursionStrategy(strategy: RecursionStrategy) {
    strategies[RecursionStrategy::class] = strategy
}
