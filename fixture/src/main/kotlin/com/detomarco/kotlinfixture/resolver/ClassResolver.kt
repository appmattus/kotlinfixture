/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.Unresolved.Companion.createUnresolved
import com.detomarco.kotlinfixture.decorator.constructor.ConstructorStrategy
import com.detomarco.kotlinfixture.decorator.constructor.RandomConstructorStrategy
import com.detomarco.kotlinfixture.strategyOrDefault
import kotlin.reflect.KClass

internal class ClassResolver : Resolver, PopulateInstance {

    @Suppress("NestedBlockDepth", "ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {
            val callContext = PopulateInstance.CallContext(
                context = context,
                obj = obj,
                constructorParameterNames = obj.constructorParameterNames(),
                callingClass = obj
            )

            val constructorStrategy = context.strategyOrDefault<ConstructorStrategy>(RandomConstructorStrategy)

            val results = constructorStrategy.constructors(context, obj).map { constructor ->
                val result = context.resolve(KFunctionRequest(obj, constructor))
                if (result !is Unresolved) {
                    return if (populatePropertiesAndSetters(callContext, result)) {
                        result
                    } else {
                        Unresolved.NotSupported("Unable to populate $obj")
                    }
                }
                result
            }

            return createUnresolved("Unable to create $obj", results)
        }

        return Unresolved.Unhandled
    }
}
