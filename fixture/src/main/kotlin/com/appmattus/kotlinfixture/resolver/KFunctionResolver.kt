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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.FixtureException
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.isAccessible

@Suppress("ComplexMethod")
internal class KFunctionResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KFunctionRequest) {
            return try {
                obj.function.isAccessible = true

                val overrides = context.configuration.properties.getOrDefault(obj.containingClass, emptyMap())

                val parameters = obj.function.parameters.associateWith {
                    if (it.kind == KParameter.Kind.VALUE) {
                        if (it.name in overrides) {
                            overrides[it.name]?.invoke()
                        } else {
                            context.resolve(it.type)
                        }
                    } else if (it.kind == KParameter.Kind.INSTANCE) {
                        obj.containingClass.companionObjectInstance
                    } else {
                        throw IllegalStateException("Unsupported parameter type: $it")
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
