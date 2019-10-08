package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.math.BigInteger
import kotlin.random.asJavaRandom

internal class BigIntegerResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == BigInteger::class) BigInteger(NUM_BITS, context.random.asJavaRandom()) else Unresolved

    companion object {
        private const val NUM_BITS = 64
    }
}
