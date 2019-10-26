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

package com.appmattus.kotlinfixture.kotlintest

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.resolver.Resolver
import io.kotlintest.properties.Gen
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

internal class KotlinTestResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {

        if (obj is KClass<*>) {
            try {
                val gen = when (obj) {
                    List::class -> {
                        val upper = obj.typeParameters.first().upperBounds.first().classifier as KClass<*>
                        @Suppress("UNCHECKED_CAST")
                        Gen.list(Gen.forClassName(upper.jvmName) as Gen<Any>)
                    }
                    Set::class -> {
                        val upper = obj.typeParameters.first().upperBounds.first().classifier as KClass<*>
                        @Suppress("UNCHECKED_CAST")
                        Gen.set(Gen.forClassName(upper.jvmName) as Gen<Any>)
                    }
                    Pair::class -> {
                        val first = obj.typeParameters[0].upperBounds.first().classifier as KClass<*>
                        val second = obj.typeParameters[1].upperBounds.first().classifier as KClass<*>
                        Gen.pair(Gen.forClassName(first.jvmName), Gen.forClassName(second.jvmName))
                    }
                    Map::class -> {
                        val first = obj.typeParameters[0].upperBounds.first().classifier as KClass<*>
                        val second = obj.typeParameters[1].upperBounds.first().classifier as KClass<*>
                        Gen.map(Gen.forClassName(first.jvmName), Gen.forClassName(second.jvmName))
                    }
                    else -> Gen.forClassName(obj.jvmName)
                }

                return gen.next()
            } catch (expected: IllegalArgumentException) {

            }
        }

        return Unresolved
    }
}
