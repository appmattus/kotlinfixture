package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.random.Random
import kotlin.reflect.jvm.isAccessible

class KFunctionResolver(private val configuration: Configuration) : Resolver {
    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KFunctionRequest) {
            return try {
                obj.function.isAccessible = true

                val overrides = configuration.properties.getOrDefault(obj.containingClass, emptyMap())

                val parameters = obj.function.parameters.associateWith {
                    if (overrides.containsKey(it.name)) {
                        overrides[it.name]
                    } else {
                        resolver.resolve(it.type, resolver)
                    }
                }.filterKeys {
                    // Keep if the parameter has an override, is mandatory, or if optional at random
                    overrides.containsKey(it.name) || !it.isOptional || Random.nextBoolean()
                }

                if (parameters.all { it.value != Unresolved }) {
                    obj.function.callBy(parameters)
                } else {
                    Unresolved
                }
            } catch (expected: Exception) {
                Unresolved
            }
        }

        return Unresolved
    }
}
