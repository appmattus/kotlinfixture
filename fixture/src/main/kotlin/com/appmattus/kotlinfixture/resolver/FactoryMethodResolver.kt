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
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.staticFunctions

@Suppress("NestedBlockDepth")
class FactoryMethodResolver : Resolver, PopulateInstance {

    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {

            (obj.companionObjectFactoryMethods() ?: obj.staticFactoryMethods()).shuffled().forEach { factoryMethod ->

                val request = KFunctionRequest(obj, factoryMethod)
                val result = context.resolve(request)

                if (result != Unresolved) {
                    if (result != null) {
                        val callContext = PopulateInstance.CallContext(
                            context,
                            result::class,
                            constructorParameterNames = emptySet(),
                            overrides = context.configuration.properties.getOrDefault(obj, emptyMap())
                        )

                        populatePropertiesAndSetters(callContext, result)
                    }

                    return result
                }
            }
        }

        return Unresolved
    }

    private fun KClass<*>.staticFactoryMethods(): List<KFunction<*>> {
        return staticFunctions.filter {
            (it.returnType.classifier as KClass<*>).isSubclassOf(this)
        }
    }

    private fun KClass<*>.companionObjectFactoryMethods(): List<KFunction<*>>? {
        return companionObject?.functions?.filter {
            (it.returnType.classifier as KClass<*>).isSubclassOf(this)
        }
    }
}
