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
import kotlin.random.nextUInt
import kotlin.random.nextULong

internal class PrimitiveResolver : Resolver {

    @Suppress("EXPERIMENTAL_API_USAGE", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = when (obj) {
        Boolean::class -> context.random.nextBoolean()

        Byte::class -> context.random.nextInt().toByte()
        Double::class -> context.random.nextDouble()
        Float::class -> context.random.nextFloat()
        Int::class -> context.random.nextInt()
        Long::class -> context.random.nextLong()
        Short::class -> context.random.nextInt().toShort()

        UByte::class -> context.random.nextUInt().toUByte()
        UInt::class -> context.random.nextUInt()
        ULong::class -> context.random.nextULong()
        UShort::class -> context.random.nextUInt().toUShort()

        else -> Unresolved.Unhandled
    }
}
