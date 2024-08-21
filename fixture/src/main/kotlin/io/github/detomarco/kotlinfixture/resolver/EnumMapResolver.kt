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
import io.github.detomarco.kotlinfixture.decorator.nullability.wrapNullability
import java.util.EnumMap
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

internal class EnumMapResolver : Resolver {

    private val enumMapConstructor by lazy {
        EnumMap::class.constructors.first {
            it.parameters.size == 1 &&
                    it.parameters[0].type.classifier?.starProjectedType == Class::class.starProjectedType
        }
    }

    private val enumMapPut by lazy {
        EnumMap::class.members.first { it.name == "put" }
    }

    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.classifier == EnumMap::class) {
                return context.wrapNullability(obj) {
                    generateEnumMap(obj)
                }
            }
        }

        return Unresolved.Unhandled
    }

    @Suppress("ReturnCount")
    private fun Context.generateEnumMap(obj: KType): Any? {
        val argType = obj.arguments.first().type!!
        val enumClass = argType.classifier as KClass<*>
        val valueClass = obj.arguments[1].type?.classifier as KClass<*>

        val enumMap = enumMapConstructor.call(enumClass.java)

        val allValues = (enumClass.members.first { it.name == "values" }.call() as Array<*>).toMutableList()

        // Verify the value class can be resolved, we check here in case the size below is zero
        resolve(valueClass).also {
            if (it is Unresolved) {
                return createUnresolved("Unable to create $valueClass", listOf(it))
            }
        }

        repeat(random.nextInt(allValues.size + 1)) {
            val index = random.nextInt(allValues.size)

            val key = allValues.removeAt(index)
            val value = resolve(valueClass)

            if (value is Unresolved) {
                return createUnresolved("Unable to create $valueClass", listOf(value))
            }

            enumMapPut.call(enumMap, key, value)
        }

        return enumMap
    }
}
