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

@file:Suppress("MatchingDeclarationName")

package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import kotlin.reflect.KType

class Fixture(val fixtureConfiguration: Configuration) {

    inline operator fun <reified T : Any?> invoke(
        range: Iterable<T> = emptyList(),
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            val result = create(typeOf<T>(), ConfigurationBuilder(fixtureConfiguration).apply(configuration).build())
            if (result is T) {
                result
            } else {
                throw UnsupportedOperationException("Unable to handle ${T::class}")
            }
        }
    }

    fun create(type: KType, configuration: Configuration): Any? {
        return ContextImpl(configuration).resolve(type)
    }
}

fun kotlinFixture(init: ConfigurationBuilder.() -> Unit = {}) =
    Fixture(ConfigurationBuilder().apply(init).build())
