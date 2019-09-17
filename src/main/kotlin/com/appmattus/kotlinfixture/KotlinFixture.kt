package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.resolver.BigDecimalResolver
import com.appmattus.kotlinfixture.resolver.BigIntegerResolver
import com.appmattus.kotlinfixture.resolver.CompositeResolver
import com.appmattus.kotlinfixture.resolver.CharResolver
import com.appmattus.kotlinfixture.resolver.EnumResolver
import com.appmattus.kotlinfixture.resolver.KTypeResolver
import com.appmattus.kotlinfixture.resolver.ObjectResolver
import com.appmattus.kotlinfixture.resolver.PrimitiveResolver
import com.appmattus.kotlinfixture.resolver.SealedClassResolver
import com.appmattus.kotlinfixture.resolver.StringResolver
import com.appmattus.kotlinfixture.resolver.UriResolver
import com.appmattus.kotlinfixture.resolver.UrlResolver
import com.appmattus.kotlinfixture.resolver.UuidResolver

class KotlinFixture {

    val resolver = CompositeResolver(
        listOf(
            CharResolver(),
            StringResolver(),
            PrimitiveResolver(),
            UrlResolver(),
            UriResolver(),
            BigDecimalResolver(),
            BigIntegerResolver(),
            UuidResolver(),
            EnumResolver(),
            ObjectResolver(),
            SealedClassResolver(),
            KTypeResolver()
        )
    )

    inline operator fun <reified T : Any?> invoke(range: Iterable<T> = emptyList()): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            val result = resolver.resolve(T::class, resolver)
            (result as? T) ?: throw UnsupportedOperationException("Unable to handle ${T::class}")
        }
    }

    fun create(any: Any): Any {
        val result = resolver.resolve(any, resolver)
        return result?.takeIf { it !is Unresolved }
            ?: throw UnsupportedOperationException("Unable to handle $any")
    }
}
