package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.FixtureException
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.jvm.isAccessible

internal class KFunctionResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KFunctionRequest) {
            return try {
                obj.function.isAccessible = true

                val overrides = context.configuration.properties.getOrDefault(obj.containingClass, emptyMap())

                val parameters = obj.function.parameters.associateWith {
                    if (it.name in overrides) {
                        overrides[it.name]?.invoke()
                    } else {
                        context.resolve(it.type)
                    }
                }.filterKeys {
                    // Keep if the parameter has an override, is mandatory, or if optional at random
                    overrides.containsKey(it.name) || !it.isOptional || context.random.nextBoolean()
                }

                if (parameters.all { it.value != Unresolved }) {
                    obj.function.callBy(parameters)
                } else {
                    Unresolved
                }
            } catch (expected: FixtureException) {
                throw expected
            } catch (expected: Exception) {
                Unresolved
            }
        }

        return Unresolved
    }
}
