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
import kotlin.reflect.KClass

internal class ClassResolver : Resolver, PopulateInstance {

    @Suppress("NestedBlockDepth")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {
            val callContext = PopulateInstance.CallContext(
                context = context,
                obj = obj,
                constructorParameterNames = obj.constructorParameterNames(),
                overrides = context.configuration.properties.getOrElse(obj) { emptyMap() }
            )

            obj.constructors.shuffled().forEach { constructor ->
                try {
                    val result = context.resolve(KFunctionRequest(obj, constructor))
                    if (result != Unresolved) {
                        return if (populatePropertiesAndSetters(callContext, result)) {
                            result
                        } else {
                            Unresolved
                        }
                    }
                } catch (expected: FixtureException) {
                    // Ignore and move on to the next function, there is a chance here of hiding recursion exceptions
                }
            }
        }

        return Unresolved
    }
}
