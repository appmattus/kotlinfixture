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
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

internal class ArrayKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier?.starProjectedType == Array<Any?>::class.starProjectedType) {
            val size = context.configuration.repeatCount()

            val type = obj.arguments[0].type!!
            val array: Any = java.lang.reflect.Array.newInstance((type.classifier as KClass<*>).java, size)

            for (i in 0 until size) {
                val element = context.resolve(type)
                if (element is Unresolved) {
                    return createUnresolved("Unable to resolve $type", listOf(element))
                }
                if (element != null) {
                    java.lang.reflect.Array.set(array, i, element)
                }
            }

            return array
        }

        return Unresolved.Unhandled
    }
}
