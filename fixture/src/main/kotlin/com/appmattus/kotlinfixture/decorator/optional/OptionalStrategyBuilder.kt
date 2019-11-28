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

package com.appmattus.kotlinfixture.decorator.optional

import com.appmattus.kotlinfixture.config.ConfigurationDsl
import com.appmattus.kotlinfixture.toUnmodifiableMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1

@ConfigurationDsl
class OptionalStrategyBuilder(private val defaultStrategy: OptionalStrategy) {

    private val classes: MutableMap<KClass<*>, OptionalStrategy> = mutableMapOf()

    private val properties: MutableMap<KClass<*>, MutableMap<String, OptionalStrategy>> = mutableMapOf()

    inline fun <reified T> classOverride(strategy: OptionalStrategy) = classOverride(T::class, strategy)

    fun classOverride(superType: KClass<*>, strategy: OptionalStrategy) {
        classes[superType] = strategy
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> propertyOverride(propertyName: String, strategy: OptionalStrategy) =
        propertyOverride(T::class, propertyName, strategy)

    inline fun <reified T> propertyOverride(property: KProperty1<T, *>, strategy: OptionalStrategy) {
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
        return propertyOverride(T::class, property.name, strategy)
    }

    fun propertyOverride(clazz: KClass<*>, propertyName: String, strategy: OptionalStrategy) {
        val classProperties = properties.getOrElse(clazz) { mutableMapOf() }
        classProperties[propertyName] = strategy

        properties[clazz] = classProperties
    }

    fun build(): OptionalStrategy = OverrideOptionalStrategy(
        defaultStrategy = defaultStrategy,
        propertyOverrides = properties.mapValues { it.value.toUnmodifiableMap() }.toUnmodifiableMap(),
        classOverrides = classes.toUnmodifiableMap()
    )
}
