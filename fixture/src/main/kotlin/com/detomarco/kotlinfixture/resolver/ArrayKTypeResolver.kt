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
import com.detomarco.kotlinfixture.Unresolved.Companion.createUnresolved
import com.detomarco.kotlinfixture.typeOf
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

internal class ArrayKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any {
        // Special handling for primitive arrays as Kotlin's KType seems to see Array<Int> as IntArray etc when looking at the classifier
        // See https://youtrack.jetbrains.com/issue/KT-52170/Reflection-typeOfArrayLong-gives-classifier-LongArray
        when (obj) {
            BooleanArrayKType -> return (context.resolve(BooleanArray::class) as BooleanArray).toTypedArray()
            ByteArrayKType -> return (context.resolve(ByteArray::class) as ByteArray).toTypedArray()
            DoubleArrayKType -> return (context.resolve(DoubleArray::class) as DoubleArray).toTypedArray()
            FloatArrayKType -> return (context.resolve(FloatArray::class) as FloatArray).toTypedArray()
            IntArrayKType -> return (context.resolve(IntArray::class) as IntArray).toTypedArray()
            LongArrayKType -> return (context.resolve(LongArray::class) as LongArray).toTypedArray()
            ShortArrayKType -> return (context.resolve(ShortArray::class) as ShortArray).toTypedArray()
            CharArrayKType -> return (context.resolve(CharArray::class) as CharArray).toTypedArray()
        }

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

    companion object {
        private val BooleanArrayKType = typeOf<Array<Boolean>>()
        private val ByteArrayKType = typeOf<Array<Byte>>()
        private val DoubleArrayKType = typeOf<Array<Double>>()
        private val FloatArrayKType = typeOf<Array<Float>>()
        private val IntArrayKType = typeOf<Array<Int>>()
        private val LongArrayKType = typeOf<Array<Long>>()
        private val ShortArrayKType = typeOf<Array<Short>>()
        private val CharArrayKType = typeOf<Array<Char>>()
    }
}
