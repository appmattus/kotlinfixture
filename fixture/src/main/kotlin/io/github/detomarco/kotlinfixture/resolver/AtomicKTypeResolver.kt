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
import io.github.detomarco.kotlinfixture.typeOf
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicIntegerArray
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicLongArray
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class AtomicKTypeResolver : Resolver {

    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = with(context) {

        if (obj is KType && obj.classifier is KClass<*>) {
            return when (obj.classifier) {
                AtomicBoolean::class -> generateAtomicBoolean(obj)
                AtomicInteger::class -> generateAtomicInteger(obj)
                AtomicLong::class -> generateAtomicLong(obj)
                AtomicIntegerArray::class -> generateAtomicIntegerArray(obj)
                AtomicLongArray::class -> generateAtomicLongArray(obj)
                AtomicReference::class -> generateAtomicReference(obj)
                else -> Unresolved.Unhandled
            }
        }

        return Unresolved.Unhandled
    }

    private fun Context.generateAtomicBoolean(obj: KType) = wrapNullability(obj) {
        AtomicBoolean(random.nextBoolean())
    }

    private fun Context.generateAtomicInteger(obj: KType) = wrapNullability(obj) {
        AtomicInteger(random.nextInt())
    }

    private fun Context.generateAtomicLong(obj: KType) = wrapNullability(obj) {
        AtomicLong(random.nextLong())
    }

    private fun Context.generateAtomicIntegerArray(obj: KType) = wrapNullability(obj) {
        AtomicIntegerArray(resolve(typeOf<IntArray>()) as IntArray)
    }

    private fun Context.generateAtomicLongArray(obj: KType) = wrapNullability(obj) {
        AtomicLongArray(resolve(typeOf<LongArray>()) as LongArray)
    }

    private fun Context.generateAtomicReference(obj: KType) = wrapNullability(obj) {
        val reference = resolve(obj.arguments.first().type!!)
        if (reference is Unresolved) {
            createUnresolved("Unable to resolve ${obj.arguments.first().type}", listOf(reference))
        } else {
            AtomicReference(reference)
        }
    }
}
