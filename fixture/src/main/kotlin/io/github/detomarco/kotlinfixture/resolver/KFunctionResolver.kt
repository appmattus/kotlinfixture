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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.Unresolved.Companion.createUnresolved
import io.github.detomarco.kotlinfixture.decorator.optional.OptionalStrategy
import io.github.detomarco.kotlinfixture.decorator.optional.RandomlyOptionalStrategy
import io.github.detomarco.kotlinfixture.strategyOrDefault
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.isAccessible

@Suppress("ComplexMethod")
internal class KFunctionResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KFunctionRequest) {
            obj.function.isAccessible = true

            val overrides = context.configuration.properties.getOrElse(obj.containingClass) { emptyMap() }

            val parameters = obj.function.parameters.associateWith {
                when (it.kind) {
                    KParameter.Kind.VALUE -> {
                        context.resolve(KNamedPropertyRequest(it.name, obj.containingClass, it.type))
                    }
                    KParameter.Kind.INSTANCE -> {
                        obj.containingClass.companionObjectInstance
                    }
                    else -> {
                        throw IllegalStateException("Unsupported parameter type: $it")
                    }
                }
            }.filterKeys {
                with(context) {
                    with(strategyOrDefault<OptionalStrategy>(RandomlyOptionalStrategy)) {
                        // Keep if the parameter has an override, is mandatory, or if optional using the strategy
                        overrides.containsKey(it.name) || !it.isOptional || !generateAsOptional(
                            obj.containingClass,
                            it.name!!
                        )
                    }
                }
            }

            return if (parameters.all { it.value !is Unresolved }) {
                try {
                    obj.function.callBy(parameters)
                } catch (expected: Exception) {
                    Unresolved.NotSupported("Unable to call function ${obj.function}")
                }
            } else {
                createUnresolved("Unable to create function ${obj.function} parameters", parameters.values.toList())
            }
        }

        return Unresolved.Unhandled
    }
}
