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

package io.github.detomarco.kotlinfixture.decorator.optional

import io.github.detomarco.kotlinfixture.config.ConfigurationDsl
import io.github.detomarco.kotlinfixture.toUnmodifiableMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1

/**
 * Builder of [OptionalStrategy], allow class and property overrides.
 */
@ConfigurationDsl
class OptionalStrategyBuilder internal constructor(private val defaultStrategy: OptionalStrategy) {

    private val classes: MutableMap<KClass<*>, OptionalStrategy> = mutableMapOf()

    private val properties: MutableMap<KClass<*>, MutableMap<String, OptionalStrategy>> = mutableMapOf()

    /**
     * Override the strategy for a particular class
     */
    @Suppress("DEPRECATION_ERROR")
    inline fun <reified T> classOverride(strategy: OptionalStrategy) = classOverride(T::class, strategy)

    @Deprecated("Use the classOverride<Class>(…) function", level = DeprecationLevel.ERROR)
    fun classOverride(superType: KClass<*>, strategy: OptionalStrategy) {
        classes[superType] = strategy
    }

    /**
     * Override the strategy for a property of a class
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified T> propertyOverride(propertyName: String, strategy: OptionalStrategy) =
        propertyOverride(T::class, propertyName, strategy)

    /**
     * Override the strategy for a property of a class
     */
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

        @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
        return propertyOverride(T::class, property.name, strategy)
    }

    @Deprecated(
        "Use the propertyOverride<Class>(propertyName, strategy) or " +
                "propertyOverride(Class::propertyName, strategy) function",
        level = DeprecationLevel.ERROR
    )
    fun propertyOverride(clazz: KClass<*>, propertyName: String, strategy: OptionalStrategy) {
        val classProperties = properties.getOrElse(clazz) { mutableMapOf() }
        classProperties[propertyName] = strategy

        properties[clazz] = classProperties
    }

    internal fun build(): OptionalStrategy = OverrideOptionalStrategy(
        defaultStrategy = defaultStrategy,
        propertyOverrides = properties.mapValues { it.value.toUnmodifiableMap() }.toUnmodifiableMap(),
        classOverrides = classes.toUnmodifiableMap()
    )
}
