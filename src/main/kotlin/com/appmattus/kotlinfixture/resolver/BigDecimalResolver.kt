package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.random.Random
import kotlin.random.asJavaRandom

class BigDecimalResolver : Resolver {

    override fun resolve(context: Context, obj: Any?): Any? {
        return if (obj == BigDecimal::class) {
            BigDecimal(BigInteger(64, Random.asJavaRandom())).divide(BigDecimal.TEN)
        } else {
            Unresolved
        }
    }
}
