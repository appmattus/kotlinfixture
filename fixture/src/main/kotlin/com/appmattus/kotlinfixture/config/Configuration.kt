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
import com.appmattus.kotlinfixture.resolver.InstanceResolver
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
import com.appmattus.kotlinfixture.toUnmodifiableList
import com.appmattus.kotlinfixture.toUnmodifiableMap
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class Configuration(
    val repeatCount: () -> Int = defaultRepeatCount,
    val properties: Map<KClass<*>, Map<String, () -> Any?>> =
        emptyMap<KClass<*>, Map<String, () -> Any?>>().toUnmodifiableMap(),
    val instances: Map<KType, Generator<Any?>.() -> Any?> =
        emptyMap<KType, Generator<Any?>.() -> Any?>().toUnmodifiableMap(),
    val subTypes: Map<KClass<*>, KClass<*>> = emptyMap<KClass<*>, KClass<*>>().toUnmodifiableMap(),
    val random: Random = defaultRandom,
    val decorators: List<Decorator> = defaultDecorators.toUnmodifiableList(),
    val resolvers: List<Resolver> = defaultResolvers.toUnmodifiableList()
) {

    private companion object {
        private val defaultRepeatCount: () -> Int = { 5 }

        private val defaultRandom = Random

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

            InstanceResolver(),
            SubTypeResolver(),

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

            AbstractClassResolver(),

            ClassResolver()
        )
    }
}
