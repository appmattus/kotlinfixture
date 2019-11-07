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
import com.appmattus.kotlinfixture.decorator.nullability.wrapNullability
import java.util.EnumSet
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class EnumSetResolver : Resolver {

    private val enumSetOf by lazy {
        EnumSet::class.members.first { it.name == "of" && it.parameters.size == 2 && it.parameters[1].isVararg }
    }

    private val enumSetNoneOf by lazy {
        EnumSet::class.members.first { it.name == "noneOf" }
    }

    @Suppress("ReturnCount", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.classifier == EnumSet::class) {
                return context.wrapNullability(obj) {
                    val argType = obj.arguments.first().type!!
                    val enumClass = argType.classifier as KClass<*>

                    val allValues = (enumClass.members.first { it.name == "values" }.call() as Array<*>).toMutableList()

                    val selected = mutableListOf<Any>()

                    repeat(random.nextInt(allValues.size + 1)) {
                        val index = random.nextInt(allValues.size)
                        selected.add(allValues.removeAt(index) as Any)
                    }

                    if (selected.isNotEmpty()) {
                        val last = selected.subList(1, selected.size).filterIsInstance<Enum<*>>().toTypedArray()
                        enumSetOf.call(selected.first(), last)
                    } else {
                        enumSetNoneOf.call(enumClass.java) as EnumSet<*>
                    }
                }
            }
        }

        return Unresolved
    }
}
