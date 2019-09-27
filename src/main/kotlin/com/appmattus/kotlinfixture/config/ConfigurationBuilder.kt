package com.appmattus.kotlinfixture.config

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class ConfigurationBuilder {
    private var configuration = Configuration()


    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    inline fun <reified T> instance(noinline generator: () -> T) = instance(typeOf<T>(), generator)

    fun instance(type: KType, generator: () -> Any?) {
        configuration = configuration.copy(instances = configuration.instances + (type to generator))
    }


    inline fun <reified T, reified U : T> subType() = subType(T::class, U::class)

    fun subType(superType: KClass<*>, subType: KClass<*>) {
        configuration = configuration.copy(subTypes = configuration.subTypes + (superType to subType))
    }


    inline fun <reified T> propertyOf(name: String, noinline generator: () -> Any?) =
        propertyOf(T::class, name, generator)

    inline fun <reified T, U> property(name: KProperty1<T, U>, noinline generator: () -> U) =
        propertyOf(T::class, name.name, generator)

    fun propertyOf(clazz: KClass<*>, name: String, generator: () -> Any?) {

        println("propertyOf: clazz=$clazz, name=$name, value=${generator()}")

        val classProperties = configuration.properties.getOrElse(clazz) { emptyMap() }

        val allProperties = configuration.properties.toMutableMap().apply {
            put(clazz, classProperties + (name to generator()))
        }

        configuration = configuration.copy(properties = allProperties)
    }


    fun repeatCount(generator: () -> Int) {
        configuration = configuration.copy(repeatCount = generator)
    }


    fun build() = configuration
}
