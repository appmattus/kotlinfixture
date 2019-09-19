package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class ClassResolver(private val configuration: Configuration) : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KClass<*>) {

            val constructorParameterNames = obj.constructorParameterNames()

            val overrides = configuration.properties.getOrDefault(obj, emptyMap())

            obj.constructors.shuffled().forEach { constructor ->
                val result = resolver.resolve(KFunctionRequest(obj, constructor), resolver)
                if (result != Unresolved) {

                    obj.settableMutableProperties()
                        .filterNot { constructorParameterNames.contains(it.name) }
                        .forEach { property ->
                            val propertyResult = overrides.getOrElse(property.name) {
                                resolver.resolve(property.returnType, resolver)
                            }

                            if (propertyResult == Unresolved) {
                                return Unresolved
                            }

                            // Property might not be visible
                            property.setter.safeCall(result, propertyResult)
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

    private fun KMutableProperty.Setter<*>.safeCall(vararg args: Any?) {
        try {
            call(*args)
        } catch (expected: Exception) {

        }
    }
}
