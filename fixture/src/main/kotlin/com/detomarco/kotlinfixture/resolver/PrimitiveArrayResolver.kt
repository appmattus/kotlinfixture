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

internal class PrimitiveArrayResolver : Resolver {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = when (obj) {
        BooleanArray::class -> context.resolveConstructor(::BooleanArray)

        ByteArray::class -> context.resolveConstructor(::ByteArray)
        DoubleArray::class -> context.resolveConstructor(::DoubleArray)
        FloatArray::class -> context.resolveConstructor(::FloatArray)
        IntArray::class -> context.resolveConstructor(::IntArray)
        LongArray::class -> context.resolveConstructor(::LongArray)
        ShortArray::class -> context.resolveConstructor(::ShortArray)
        CharArray::class -> context.resolveConstructor(::CharArray)

        UByteArray::class -> context.resolveConstructor(::UByteArray)
        UIntArray::class -> context.resolveConstructor(::UIntArray)
        ULongArray::class -> context.resolveConstructor(::ULongArray)
        UShortArray::class -> context.resolveConstructor(::UShortArray)

        else -> Unresolved.Unhandled
    }

    private inline fun <A, reified T> Context.resolveConstructor(param: (size: Int, init: (Int) -> T) -> A): A {
        return param(configuration.repeatCount()) {
            // TODO what if one of these is unresolvable?
            resolve(T::class) as T
        }
    }
}
