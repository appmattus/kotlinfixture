package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

internal class ClassResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {
            val constructorParameterNames = obj.constructorParameterNames()

            val overrides = context.configuration.properties.getOrDefault(obj, emptyMap())

            obj.constructors.shuffled().forEach { constructor ->
                val result = context.resolve(KFunctionRequest(obj, constructor))
                if (result != Unresolved) {

                    try {
                        obj.settableMutableProperties()
                            .filterNot { constructorParameterNames.contains(it.name) }
                            .forEach { property ->
                                val propertyResult = if (property.name in overrides) {
                                    overrides[property.name]?.invoke()
                                } else {
                                    context.resolve(property.returnType)
                                }

                                if (propertyResult == Unresolved) {
                                    return Unresolved
                                }

                                // Property might not be visible
                                property.setter.safeCall(result, propertyResult)
                            }
                    } catch (expected: Error) {
                        // If retrieving setters fails we fail regardless of constructor used
                        return Unresolved
                    }

                    return result
                }
            }
        }

        return Unresolved
    }

    private fun KClass<*>.settableMutableProperties(): List<KMutableProperty<*>> {
        return memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.setter.visibility != KVisibility.PRIVATE }
    }

    private fun KClass<*>.constructorParameterNames(): Set<String?> {
        return constructors.flatMap { constructor ->
            constructor.parameters.map { it.name }
        }.toSet()
    }

    private fun KMutableProperty.Setter<*>.safeCall(obj: Any?, value: Any?) {
        try {
            call(obj, value)
        } catch (expected: Exception) {
            // ignored
        }
    }
}
