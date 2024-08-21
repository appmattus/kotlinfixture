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
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class TupleKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            return when (obj.classifier as KClass<*>) {
                Pair::class -> generatePair(context, obj)
                Triple::class -> generateTriple(context, obj)
                else -> Unresolved.Unhandled
            }
        }

        return Unresolved.Unhandled
    }

    private fun generatePair(context: Context, obj: KType): Any? = context.wrapNullability(obj) {
        val firstType = obj.arguments[0].type!!
        val secondType = obj.arguments[1].type!!

        val first = resolve(firstType)
        val second = resolve(secondType)

        when {
            first is Unresolved -> createUnresolved("Unable to create Pair first $firstType", listOf(first))
            second is Unresolved -> createUnresolved("Unable to create Pair second $secondType", listOf(second))

            else -> first to second
        }
    }

    private fun generateTriple(context: Context, obj: KType): Any? = context.wrapNullability(obj) {
        val firstType = obj.arguments[0].type!!
        val secondType = obj.arguments[1].type!!
        val thirdType = obj.arguments[2].type!!

        val first = resolve(firstType)
        val second = resolve(secondType)
        val third = resolve(thirdType)

        when {
            first is Unresolved -> createUnresolved("Unable to create Triple first $firstType", listOf(first))
            second is Unresolved -> createUnresolved("Unable to create Triple second $secondType", listOf(second))
            third is Unresolved -> createUnresolved("Unable to create Triple third $thirdType", listOf(third))

            else -> Triple(first, second, third)
        }
    }
}
