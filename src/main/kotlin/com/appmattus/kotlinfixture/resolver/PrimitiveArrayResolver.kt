package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration

class PrimitiveArrayResolver(private val configuration: Configuration) : Resolver {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun resolve(obj: Any?, resolver: Resolver): Any? = when (obj) {
        BooleanArray::class -> resolver.resolve(::BooleanArray)

        ByteArray::class -> resolver.resolve(::ByteArray)
        DoubleArray::class -> resolver.resolve(::DoubleArray)
        FloatArray::class -> resolver.resolve(::FloatArray)
        IntArray::class -> resolver.resolve(::IntArray)
        LongArray::class -> resolver.resolve(::LongArray)
        ShortArray::class -> resolver.resolve(::ShortArray)

        UByteArray::class -> resolver.resolve(::UByteArray)
        UIntArray::class -> resolver.resolve(::UIntArray)
        ULongArray::class -> resolver.resolve(::ULongArray)
        UShortArray::class -> resolver.resolve(::UShortArray)

        else -> Unresolved
    }

    private inline fun <A, reified T> Resolver.resolve(param: (size: Int, init: (Int) -> T) -> A): A {
        return param(configuration.repeatCount()) {
            resolve(T::class, this) as T
        }
    }
}
