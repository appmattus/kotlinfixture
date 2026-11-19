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
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary

/**
 * Creates an [Arb] using Kotest's random source so its seed can replay generated inputs.
 * The fixture's other configuration is retained. No shrinking is performed by default.
 */
inline fun <reified T> Fixture.kotestGen(): Arb<T> = kotestGen(Shrinker { emptyList() })

/**
 * Creates an [Arb] using Kotest's random source and the supplied [shrinker].
 * Shrink candidates should preserve any constraints required by the property.
 */
inline fun <reified T> Fixture.kotestGen(shrinker: Shrinker<T>): Arb<T> = arbitrary(shrinker) { source ->
    this@kotestGen.invoke<T> { random = source.random }
}
