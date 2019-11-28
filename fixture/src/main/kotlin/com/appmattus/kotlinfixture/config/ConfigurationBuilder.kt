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

@ConfigurationDsl
class ConfigurationBuilder(configuration: Configuration = Configuration()) {

    var decorators: MutableList<Decorator> = configuration.decorators.toMutableList()
    var resolvers: MutableList<Resolver> = configuration.resolvers.toMutableList()
    var random: Random = configuration.random

    private var repeatCount: () -> Int = configuration.repeatCount
    private val properties: MutableMap<KClass<*>, MutableMap<String, GeneratorFun>> =
        configuration.properties.mapValues { it.value.toMutableMap() }.toMutableMap()
    private val factories: MutableMap<KType, GeneratorFun> = configuration.factories.toMutableMap()
    private val subTypes: MutableMap<KClass<*>, KClass<*>> = configuration.subTypes.toMutableMap()

    internal val strategies: MutableMap<KClass<*>, Any> = configuration.strategies.toMutableMap()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> factory(noinline generator: Generator<T>.() -> T) =
        factory(typeOf<T>(), generator as GeneratorFun)

    fun factory(type: KType, generator: GeneratorFun) {
        factories[type] = generator
    }

    inline fun <reified T, reified U : T> subType() = subType(T::class, U::class)

    fun subType(superType: KClass<*>, subType: KClass<*>) {
        subTypes[superType] = subType
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T, G> property(propertyName: String, noinline generator: Generator<G>.() -> G) =
        property(T::class, propertyName, generator as GeneratorFun)

    inline fun <reified T, G> property(property: KProperty1<T, G>, noinline generator: Generator<G>.() -> G) {
        // Only allow read only properties in constructor(s)
        if (property !is KMutableProperty1) {
            val constructorParams = T::class.constructors.flatMap {
                it.parameters.map(KParameter::name)
            }

            check(constructorParams.contains(property.name)) {
                "No setter available for ${T::class.qualifiedName}.${property.name}"
            }
        }

        @Suppress("UNCHECKED_CAST")
        return property(T::class, property.name, generator as GeneratorFun)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified G> property(function: KFunction<Unit>, noinline generator: Generator<G>.() -> G) = property(
        function.parameters[0].type.classifier as KClass<*>,
        function.name,
        generator as GeneratorFun
    )

    fun property(clazz: KClass<*>, propertyName: String, generator: GeneratorFun) {
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
        factories = factories.toUnmodifiableMap(),
        subTypes = subTypes.toUnmodifiableMap(),
        random = random,
        decorators = decorators.toUnmodifiableList(),
        resolvers = resolvers.toUnmodifiableList(),
        strategies = strategies.toUnmodifiableMap()
    )
}
