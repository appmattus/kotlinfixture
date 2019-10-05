package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.decorator.recursion.RecursionDecorator
import com.appmattus.kotlinfixture.decorator.recursion.ThrowingRecursionStrategy
import com.appmattus.kotlinfixture.resolver.AbstractClassResolver
import com.appmattus.kotlinfixture.resolver.ArrayResolver
import com.appmattus.kotlinfixture.resolver.BigDecimalResolver
import com.appmattus.kotlinfixture.resolver.BigIntegerResolver
import com.appmattus.kotlinfixture.resolver.CalendarResolver
import com.appmattus.kotlinfixture.resolver.CharResolver
import com.appmattus.kotlinfixture.resolver.ClassResolver
import com.appmattus.kotlinfixture.resolver.DateResolver
import com.appmattus.kotlinfixture.resolver.EnumMapResolver
import com.appmattus.kotlinfixture.resolver.EnumResolver
import com.appmattus.kotlinfixture.resolver.EnumSetResolver
import com.appmattus.kotlinfixture.resolver.HashtableKTypeResolver
import com.appmattus.kotlinfixture.resolver.IterableKTypeResolver
import com.appmattus.kotlinfixture.resolver.KFunctionResolver
import com.appmattus.kotlinfixture.resolver.KTypeResolver
import com.appmattus.kotlinfixture.resolver.MapKTypeResolver
import com.appmattus.kotlinfixture.resolver.ObjectResolver
import com.appmattus.kotlinfixture.resolver.PrimitiveArrayResolver
import com.appmattus.kotlinfixture.resolver.PrimitiveResolver
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.resolver.SealedClassResolver
import com.appmattus.kotlinfixture.resolver.StringResolver
import com.appmattus.kotlinfixture.resolver.SubTypeResolver
import com.appmattus.kotlinfixture.resolver.UriResolver
import com.appmattus.kotlinfixture.resolver.UrlResolver
import com.appmattus.kotlinfixture.resolver.UuidResolver
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class Configuration(
    val dateSpecification: DateSpecification = defaultDateSpecification,
    val repeatCount: () -> Int = defaultRepeatCount,
    val properties: Map<KClass<*>, Map<String, () -> Any?>> = emptyMap(),
    val instances: Map<KType, () -> Any?> = emptyMap(),
    val subTypes: Map<KClass<*>, KClass<*>> = emptyMap(),
    val decorators: List<Decorator> = defaultDecorators,
    val resolvers: List<Resolver> = defaultResolvers
) {

    companion object {
        private val defaultRepeatCount: () -> Int = { 5 }

        private val defaultDateSpecification: DateSpecification = DateSpecification.Between(
            Date(Date().time - TimeUnit.DAYS.toMillis(365)),
            Date(Date().time + TimeUnit.DAYS.toMillis(365))
        )

        private val defaultDecorators = listOf(RecursionDecorator(ThrowingRecursionStrategy()))

        private val defaultResolvers = listOf(
            CharResolver(),
            StringResolver(),
            PrimitiveResolver(),
            UrlResolver(),
            UriResolver(),
            BigDecimalResolver(),
            BigIntegerResolver(),
            UuidResolver(),
            EnumResolver(),
            CalendarResolver(),
            DateResolver(),

            ObjectResolver(),
            SealedClassResolver(),

            ArrayResolver(),

            PrimitiveArrayResolver(),
            HashtableKTypeResolver(),
            IterableKTypeResolver(),
            EnumSetResolver(),
            EnumMapResolver(),
            MapKTypeResolver(),
            KTypeResolver(),
            KFunctionResolver(),

            SubTypeResolver(),

            AbstractClassResolver(),

            ClassResolver()
        )
    }
}
