package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.random.asJavaRandom

internal class BigDecimalResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj == BigDecimal::class) {
            BigDecimal(BigInteger(NUM_BITS, context.random.asJavaRandom())).divide(BigDecimal.TEN)
        } else {
            Unresolved
        }
    }

    companion object {
        private const val NUM_BITS = 64
    }
}
