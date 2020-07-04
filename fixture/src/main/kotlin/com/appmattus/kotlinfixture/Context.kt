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

package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.random.Random

/**
 * Used in [Resolver] to provide the current [configuration], access to the [resolver] chain for nested resolving and
 * convenient access to [random].
 */
interface Context {
    val configuration: Configuration
    val resolver: Resolver

    val random: Random
        get() = configuration.random

    /**
     * [resolve] the [obj] in the [resolver] chain.
     */
    fun resolve(obj: Any) = resolver.resolve(this, obj)
}

/**
 * Return the configuration for a given strategy or [default] if none is found
 * @suppress
 */
inline fun <reified T> Context.strategyOrDefault(default: T): T = configuration.strategies[T::class] as? T ?: default
