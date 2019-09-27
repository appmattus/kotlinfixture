package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

class PrimitiveResolver : Resolver {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun resolve(context: Context, obj: Any?): Any? = when (obj) {
        Boolean::class -> Random.nextBoolean()

        Byte::class -> Random.nextInt().toByte()
        Double::class -> Random.nextDouble()
        Float::class -> Random.nextFloat()
        Int::class -> Random.nextInt()
        Long::class -> Random.nextLong()
        Short::class -> Random.nextInt().toShort()

        UByte::class -> Random.nextUInt().toUByte()
        UInt::class -> Random.nextUInt()
        ULong::class -> Random.nextULong()
        UShort::class -> Random.nextUInt().toUShort()

        else -> Unresolved
    }
}
