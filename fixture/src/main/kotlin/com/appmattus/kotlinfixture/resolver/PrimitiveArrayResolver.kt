package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved

class PrimitiveArrayResolver : Resolver {

    @Suppress("EXPERIMENTAL_API_USAGE", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? = when (obj) {
        BooleanArray::class -> context.resolveConstructor(::BooleanArray)

        ByteArray::class -> context.resolveConstructor(::ByteArray)
        DoubleArray::class -> context.resolveConstructor(::DoubleArray)
        FloatArray::class -> context.resolveConstructor(::FloatArray)
        IntArray::class -> context.resolveConstructor(::IntArray)
        LongArray::class -> context.resolveConstructor(::LongArray)
        ShortArray::class -> context.resolveConstructor(::ShortArray)

        UByteArray::class -> context.resolveConstructor(::UByteArray)
        UIntArray::class -> context.resolveConstructor(::UIntArray)
        ULongArray::class -> context.resolveConstructor(::ULongArray)
        UShortArray::class -> context.resolveConstructor(::UShortArray)

        else -> Unresolved
    }

    private inline fun <A, reified T> Context.resolveConstructor(param: (size: Int, init: (Int) -> T) -> A): A {
        return param(configuration.repeatCount()) {
            resolve(T::class) as T
        }
    }
}
