package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.toUnmodifiableList
import com.appmattus.kotlinfixture.toUnmodifiableMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class ConfigurationBuilder(configuration: Configuration = Configuration()) {

    var dateSpecification: DateSpecification = configuration.dateSpecification
    var decorators: MutableList<Decorator> = configuration.decorators.toMutableList()
    var resolvers: MutableList<Resolver> = configuration.resolvers.toMutableList()

    private var repeatCount: () -> Int = configuration.repeatCount
    private val properties: MutableMap<KClass<*>, MutableMap<String, () -> Any?>> =
        configuration.properties.mapValues { it.value.toMutableMap() }.toMutableMap()
    private val instances: MutableMap<KType, () -> Any?> = configuration.instances.toMutableMap()
    private val subTypes: MutableMap<KClass<*>, KClass<*>> = configuration.subTypes.toMutableMap()

    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    inline fun <reified T> instance(noinline generator: () -> T) = instance(typeOf<T>(), generator)

    fun instance(type: KType, generator: () -> Any?) {
        instances[type] = generator
    }

    inline fun <reified T, reified U : T> subType() = subType(T::class, U::class)

    fun subType(superType: KClass<*>, subType: KClass<*>) {
        subTypes[superType] = subType
    }

    inline fun <reified T> propertyOf(name: String, noinline generator: () -> Any?) =
        propertyOf(T::class, name, generator)

    inline fun <reified T, U> property(name: KProperty1<T, U>, noinline generator: () -> U) =
        propertyOf(T::class, name.name, generator)

    fun propertyOf(clazz: KClass<*>, name: String, generator: () -> Any?) {
        val classProperties = properties.getOrElse(clazz) { mutableMapOf() }
        classProperties[name] = generator

        properties[clazz] = classProperties
    }

    fun repeatCount(generator: () -> Int) {
        repeatCount = generator
    }

    fun build() = Configuration(
        dateSpecification = dateSpecification,
        repeatCount = repeatCount,
        properties = properties.mapValues { it.value.toUnmodifiableMap() }.toUnmodifiableMap(),
        instances = instances.toUnmodifiableMap(),
        subTypes = subTypes.toUnmodifiableMap(),
        decorators = decorators.toUnmodifiableList(),
        resolvers = resolvers.toUnmodifiableList()
    )
}
