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

package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.toUnmodifiableList
import com.appmattus.kotlinfixture.toUnmodifiableMap
import com.appmattus.kotlinfixture.typeOf
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

class ConfigurationBuilder(configuration: Configuration = Configuration()) {

    var decorators: MutableList<Decorator> = configuration.decorators.toMutableList()
    var resolvers: MutableList<Resolver> = configuration.resolvers.toMutableList()
    var random: Random = configuration.random

    private var repeatCount: () -> Int = configuration.repeatCount
    private val properties: MutableMap<KClass<*>, MutableMap<String, () -> Any?>> =
        configuration.properties.mapValues { it.value.toMutableMap() }.toMutableMap()
    private val instances: MutableMap<KType, Generator<Any?>.() -> Any?> = configuration.instances.toMutableMap()
    private val subTypes: MutableMap<KClass<*>, KClass<*>> = configuration.subTypes.toMutableMap()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> instance(noinline generator: Generator<T>.() -> T) =
        instance(typeOf<T>(), generator as Generator<Any?>.() -> Any?)

    fun instance(type: KType, generator: Generator<Any?>.() -> Any?) {
        instances[type] = generator
    }

    inline fun <reified T, reified U : T> subType() = subType(T::class, U::class)

    fun subType(superType: KClass<*>, subType: KClass<*>) {
        subTypes[superType] = subType
    }

    inline fun <reified T> property(propertyName: String, noinline generator: () -> Any?) =
        property(T::class, propertyName, generator)

    inline fun <reified T, U> property(property: KProperty1<T, U>, noinline generator: () -> U) {
        // Only allow read only properties in constructor(s)
        if (property !is KMutableProperty1) {
            val constructorParams = T::class.constructors.flatMap {
                it.parameters.map(KParameter::name)
            }

            check(constructorParams.contains(property.name)) {
                "No setter available for ${T::class.qualifiedName}.${property.name}"
            }
        }

        return property(T::class, property.name, generator)
    }

    inline fun <reified U> property(function: KFunction<Unit>, noinline generator: () -> U) =
        property(function.parameters[0].type.classifier as KClass<*>, function.name, generator)

    fun property(clazz: KClass<*>, propertyName: String, generator: () -> Any?) {
        val classProperties = properties.getOrElse(clazz) { mutableMapOf() }
        classProperties[propertyName] = generator

        properties[clazz] = classProperties
    }

    fun repeatCount(generator: () -> Int) {
        repeatCount = generator
    }

    fun build() = Configuration(
        repeatCount = repeatCount,
        properties = properties.mapValues { it.value.toUnmodifiableMap() }.toUnmodifiableMap(),
        instances = instances.toUnmodifiableMap(),
        subTypes = subTypes.toUnmodifiableMap(),
        random = random,
        decorators = decorators.toUnmodifiableList(),
        resolvers = resolvers.toUnmodifiableList()
    )
}
