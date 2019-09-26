package com.appmattus.kotlinfixture.config

import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class Configuration(
    val dateSpecification: DateSpecification = defaultDateSpecification,
    val repeatCount: () -> Int = defaultRepeatCount,
    val properties: Map<KClass<*>, Map<String, Any?>> = emptyMap(),
    val instances: Map<KType, () -> Any?> = emptyMap(),
    val subTypes: Map<KClass<*>, KClass<*>> = emptyMap()
) {
    operator fun plus(other: Configuration): Configuration {
        val newDateSpecification = if (other.dateSpecification !== defaultDateSpecification) {
            other.dateSpecification
        } else {
            dateSpecification
        }

        val newRepeatCount = if (other.repeatCount !== defaultRepeatCount) {
            other.repeatCount
        } else {
            repeatCount
        }

        val newProperties = mutableMapOf<KClass<*>, Map<String, Any?>>()
        newProperties.putAll(properties)
        other.properties.forEach { (clazz, properties) ->
            newProperties.compute(clazz) { _, origProperties ->
                origProperties?.let { it + properties } ?: properties
            }
        }

        val newInstances = instances + other.instances
        val newSubTypes = subTypes + other.subTypes

        return Configuration(newDateSpecification, newRepeatCount, newProperties, newInstances, newSubTypes)
    }

    companion object {
        internal val defaultRepeatCount: () -> Int = { 5 }

        internal val defaultDateSpecification: DateSpecification = DateSpecification.Between(
            Date(Date().time - TimeUnit.DAYS.toMillis(365)),
            Date(Date().time + TimeUnit.DAYS.toMillis(365))
        )
    }
}
