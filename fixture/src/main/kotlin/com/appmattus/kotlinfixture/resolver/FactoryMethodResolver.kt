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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.Unresolved.Companion.createUnresolved
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.staticFunctions

@Suppress("NestedBlockDepth")
internal class FactoryMethodResolver : Resolver, PopulateInstance {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {

            val results =
                (obj.companionObjectFactoryMethods() ?: obj.staticFactoryMethods()).shuffled().map { factoryMethod ->

                    val request = KFunctionRequest(obj, factoryMethod)
                    val result = context.resolve(request)

                    if (result !is Unresolved) {
                        if (result != null) {
                            val callContext = PopulateInstance.CallContext(
                                context = context,
                                obj = result::class,
                                constructorParameterNames = emptySet(),
                                callingClass = obj
                            )

                            populatePropertiesAndSetters(callContext, result)
                        }

                        return result
                    }

                    result
                }

            return createUnresolved("Unable to instantiate $obj using factory methods", results)
        }

        return Unresolved.Unhandled
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
