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

package com.detomarco.kotlinfixture.config

import com.detomarco.kotlinfixture.decorator.Decorator
import com.detomarco.kotlinfixture.decorator.exception.ExceptionDecorator
import com.detomarco.kotlinfixture.decorator.filter.Filter
import com.detomarco.kotlinfixture.decorator.filter.FilterDecorator
import com.detomarco.kotlinfixture.decorator.logging.LoggingDecorator
import com.detomarco.kotlinfixture.decorator.recursion.RecursionDecorator
import com.detomarco.kotlinfixture.resolver.AbstractClassResolver
import com.detomarco.kotlinfixture.resolver.ArrayKTypeResolver
import com.detomarco.kotlinfixture.resolver.AtomicKTypeResolver
import com.detomarco.kotlinfixture.resolver.BigDecimalResolver
import com.detomarco.kotlinfixture.resolver.BigIntegerResolver
import com.detomarco.kotlinfixture.resolver.CalendarResolver
import com.detomarco.kotlinfixture.resolver.CharResolver
import com.detomarco.kotlinfixture.resolver.ClassResolver
import com.detomarco.kotlinfixture.resolver.CurrencyResolver
import com.detomarco.kotlinfixture.resolver.DateResolver
import com.detomarco.kotlinfixture.resolver.EnumMapResolver
import com.detomarco.kotlinfixture.resolver.EnumResolver
import com.detomarco.kotlinfixture.resolver.EnumSetResolver
import com.detomarco.kotlinfixture.resolver.FactoryMethodResolver
import com.detomarco.kotlinfixture.resolver.FactoryResolver
import com.detomarco.kotlinfixture.resolver.FakeResolver
import com.detomarco.kotlinfixture.resolver.FileResolver
import com.detomarco.kotlinfixture.resolver.FormatResolver
import com.detomarco.kotlinfixture.resolver.HashtableKTypeResolver
import com.detomarco.kotlinfixture.resolver.IterableKTypeResolver
import com.detomarco.kotlinfixture.resolver.JodaTimeResolver
import com.detomarco.kotlinfixture.resolver.KFunctionResolver
import com.detomarco.kotlinfixture.resolver.KNamedPropertyResolver
import com.detomarco.kotlinfixture.resolver.KTormResolver
import com.detomarco.kotlinfixture.resolver.KTypeResolver
import com.detomarco.kotlinfixture.resolver.LocaleResolver
import com.detomarco.kotlinfixture.resolver.MapKTypeResolver
import com.detomarco.kotlinfixture.resolver.ObjectResolver
import com.detomarco.kotlinfixture.resolver.PrimitiveArrayResolver
import com.detomarco.kotlinfixture.resolver.PrimitiveResolver
import com.detomarco.kotlinfixture.resolver.Resolver
import com.detomarco.kotlinfixture.resolver.SealedClassResolver
import com.detomarco.kotlinfixture.resolver.StringResolver
import com.detomarco.kotlinfixture.resolver.SubTypeResolver
import com.detomarco.kotlinfixture.resolver.ThreeTenResolver
import com.detomarco.kotlinfixture.resolver.TimeResolver
import com.detomarco.kotlinfixture.resolver.TupleKTypeResolver
import com.detomarco.kotlinfixture.resolver.UriResolver
import com.detomarco.kotlinfixture.resolver.UrlResolver
import com.detomarco.kotlinfixture.resolver.UuidResolver
import com.detomarco.kotlinfixture.toUnmodifiableList
import com.detomarco.kotlinfixture.toUnmodifiableMap
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * The [Configuration] for generating the current fixture. This is a combination of all previous configurations.
 * @property repeatCount The length used for lists and maps.
 * @property propertiesRepeatCount Overrides the length used for lists and maps on constructor parameters and mutable properties when generating instances of generic classes.
 * @property properties Overrides for constructor parameters and mutable properties when generating instances of generic classes.
 * @property factories Given instances for a particular class using a factory method.
 * @property subTypes Superclass to subclass mapping for subtypes.
 * @property random Random to use for generating random values. This may be a seeded random.
 * @property decorators Each [Decorator] wraps the resolver chain.
 * @property resolvers The resolver chain, each [Resolver] is called in order until one handles the input object.
 * @property strategies Strategy settings for altering the behaviour of [resolvers] and [decorators].
 * @property filters Sequence filters for generated values.
 */
data class Configuration internal constructor(
    val repeatCount: () -> Int = defaultRepeatCount,
    val propertiesRepeatCount: Map<KClass<*>, Map<String, () -> Int>> =
        emptyMap<KClass<*>, Map<String, () -> Int>>().toUnmodifiableMap(),
    val properties: Map<KClass<*>, Map<String, GeneratorFun>> =
        emptyMap<KClass<*>, Map<String, GeneratorFun>>().toUnmodifiableMap(),
    val factories: Map<KType, GeneratorFun> =
        emptyMap<KType, GeneratorFun>().toUnmodifiableMap(),
    val subTypes: Map<KClass<*>, KClass<*>> = emptyMap<KClass<*>, KClass<*>>().toUnmodifiableMap(),
    val random: Random = defaultRandom,
    val decorators: List<Decorator> = defaultDecorators.toUnmodifiableList(),
    val resolvers: List<Resolver> = defaultResolvers.toUnmodifiableList(),
    val strategies: Map<KClass<*>, Any> = emptyMap<KClass<*>, Any>().toUnmodifiableMap(),
    internal val filters: Map<KType, Filter> = emptyMap<KType, Filter>().toUnmodifiableMap()
) {

    private companion object {
        private val defaultRepeatCount: () -> Int = { 5 }

        private val defaultRandom = Random

        private val defaultDecorators = listOf(
            FilterDecorator(),
            ExceptionDecorator(),
            RecursionDecorator(),
            LoggingDecorator()
        )

        private val defaultResolvers = listOf(
            FactoryResolver(),
            SubTypeResolver(),

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
            TimeResolver(),
            JodaTimeResolver(),
            ThreeTenResolver(),
            FileResolver(),
            FormatResolver(),
            CurrencyResolver(),
            LocaleResolver(),
            KTormResolver(),

            ObjectResolver(),
            SealedClassResolver(),

            AtomicKTypeResolver(),
            TupleKTypeResolver(),

            ArrayKTypeResolver(),
            PrimitiveArrayResolver(),
            HashtableKTypeResolver(),
            IterableKTypeResolver(),
            EnumSetResolver(),
            EnumMapResolver(),
            MapKTypeResolver(),

            KTypeResolver(),
            FakeResolver(),
            KNamedPropertyResolver(),
            KFunctionResolver(),

            AbstractClassResolver(),

            ClassResolver(),
            FactoryMethodResolver()
        )
    }
}
