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
import io.kotest.property.arbitrary.arbitrary

/**
 * Creates a new [Arb] that performs no shrinking, and generates values for the given type, [T].
 * @suppress
 */
inline fun <reified T> Fixture.kotestGen(): Arb<T> = arbitrary { this() }
