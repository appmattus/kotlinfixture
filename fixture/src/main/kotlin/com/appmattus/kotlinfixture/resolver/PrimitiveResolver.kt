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
import kotlin.reflect.KClass

internal class PrimitiveResolver : Resolver {

    @Suppress("EXPERIMENTAL_API_USAGE", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = when ((obj as? KClass<*>)?.javaObjectType) {
        Boolean::class.javaObjectType -> context.random.nextBoolean()

        Byte::class.javaObjectType -> context.random.nextInt().toByte()
        Double::class.javaObjectType -> context.random.nextDouble()
        Float::class.javaObjectType -> context.random.nextFloat()
        Int::class.javaObjectType -> context.random.nextInt()
        Long::class.javaObjectType -> context.random.nextLong()
        Short::class.javaObjectType -> context.random.nextInt().toShort()

        UByte::class.javaObjectType -> context.random.nextUInt().toUByte()
        UInt::class.javaObjectType -> context.random.nextUInt()
        ULong::class.javaObjectType -> context.random.nextULong()
        UShort::class.javaObjectType -> context.random.nextUInt().toUShort()

        else -> Unresolved.Unhandled
    }
}
