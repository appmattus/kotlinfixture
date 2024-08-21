/*
 * Copyright 2019 Appmattus Limited
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

package io.github.detomarco.kotlinfixture

import io.github.detomarco.kotlinfixture.config.Configuration
import io.github.detomarco.kotlinfixture.resolver.Resolver
import kotlin.random.Random

data class TestContext(override val configuration: Configuration, override val resolver: Resolver) : Context {
    fun seedRandom() = this.copy(configuration = configuration.copy(random = Random(0)))
}
