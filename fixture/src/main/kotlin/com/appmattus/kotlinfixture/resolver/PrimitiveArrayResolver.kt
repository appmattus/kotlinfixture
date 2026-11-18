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

internal class PrimitiveArrayResolver : Resolver {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = when ((obj as? KClass<*>)?.javaObjectType) {
        BooleanArray::class.java -> context.resolveConstructor(::BooleanArray)

        ByteArray::class.java -> context.resolveConstructor(::ByteArray)
        DoubleArray::class.java -> context.resolveConstructor(::DoubleArray)
        FloatArray::class.java -> context.resolveConstructor(::FloatArray)
        IntArray::class.java -> context.resolveConstructor(::IntArray)
        LongArray::class.java -> context.resolveConstructor(::LongArray)
        ShortArray::class.java -> context.resolveConstructor(::ShortArray)
        CharArray::class.java -> context.resolveConstructor(::CharArray)

        UByteArray::class.java -> context.resolveConstructor(::UByteArray)
        UIntArray::class.java -> context.resolveConstructor(::UIntArray)
        ULongArray::class.java -> context.resolveConstructor(::ULongArray)
        UShortArray::class.java -> context.resolveConstructor(::UShortArray)

        else -> Unresolved.Unhandled
    }

    private inline fun <A, reified T> Context.resolveConstructor(param: (size: Int, init: (Int) -> T) -> A): A {
        return param(configuration.repeatCount()) {
            // TODO what if one of these is unresolvable?
            resolve(T::class) as T
        }
    }
}
