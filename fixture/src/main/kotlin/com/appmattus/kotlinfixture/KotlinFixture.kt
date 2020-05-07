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

@file:Suppress("MatchingDeclarationName")

package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class Fixture @JvmOverloads constructor(val fixtureConfiguration: Configuration = ConfigurationBuilder().build()) {

    inline operator fun <reified T : Any?> invoke(
        range: Iterable<T> = emptyList(),
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            create(typeOf<T>(), ConfigurationBuilder(fixtureConfiguration).apply(configuration).build()) as T
        }
    }

    inline fun <reified T : Any?> asSequence(
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): Sequence<T> {
        val type = typeOf<T>()
        val builtConfiguration = ConfigurationBuilder(fixtureConfiguration).apply(configuration).build()
        return sequence {
            while (true) {
                yield(create(type, builtConfiguration) as T)
            }
        }
    }

    @JvmOverloads
    fun create(clazz: Class<*>, configuration: Configuration = fixtureConfiguration): Any? {
        return create(clazz.kotlin, configuration)
    }

    @JvmOverloads
    fun create(clazz: KClass<*>, configuration: Configuration = fixtureConfiguration): Any? {
        return create(clazz.starProjectedType, configuration)
    }

    @JvmOverloads
    fun create(type: KType, configuration: Configuration = fixtureConfiguration): Any? {
        val result = ContextImpl(configuration).resolve(type)
        if (result is Unresolved) {
            throw UnsupportedOperationException("Unable to handle $type\n$result")
        }
        return result
    }

    fun new(configuration: ConfigurationBuilder.() -> Unit = {}): Fixture {
        return Fixture(ConfigurationBuilder(fixtureConfiguration).apply(configuration).build())
    }
}

fun kotlinFixture(init: ConfigurationBuilder.() -> Unit = {}) = Fixture(ConfigurationBuilder().apply(init).build())
