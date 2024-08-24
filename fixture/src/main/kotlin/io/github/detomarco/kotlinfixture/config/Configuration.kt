/*
 * Copyright 2021 Appmattus Limited
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

package io.github.detomarco.kotlinfixture.config

import io.github.detomarco.kotlinfixture.decorator.Decorator
import io.github.detomarco.kotlinfixture.decorator.exception.ExceptionDecorator
import io.github.detomarco.kotlinfixture.decorator.filter.Filter
import io.github.detomarco.kotlinfixture.decorator.filter.FilterDecorator
import io.github.detomarco.kotlinfixture.decorator.logging.LoggingDecorator
import io.github.detomarco.kotlinfixture.decorator.recursion.RecursionDecorator
import io.github.detomarco.kotlinfixture.resolver.AbstractClassResolver
import io.github.detomarco.kotlinfixture.resolver.ArrayKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.AtomicKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.BigDecimalResolver
import io.github.detomarco.kotlinfixture.resolver.BigIntegerResolver
import io.github.detomarco.kotlinfixture.resolver.CalendarResolver
import io.github.detomarco.kotlinfixture.resolver.CharResolver
import io.github.detomarco.kotlinfixture.resolver.ClassResolver
import io.github.detomarco.kotlinfixture.resolver.CurrencyResolver
import io.github.detomarco.kotlinfixture.resolver.DateResolver
import io.github.detomarco.kotlinfixture.resolver.EnumMapResolver
import io.github.detomarco.kotlinfixture.resolver.EnumResolver
import io.github.detomarco.kotlinfixture.resolver.EnumSetResolver
import io.github.detomarco.kotlinfixture.resolver.FactoryMethodResolver
import io.github.detomarco.kotlinfixture.resolver.FactoryResolver
import io.github.detomarco.kotlinfixture.resolver.FakeResolver
import io.github.detomarco.kotlinfixture.resolver.FileResolver
import io.github.detomarco.kotlinfixture.resolver.FormatResolver
import io.github.detomarco.kotlinfixture.resolver.HashtableKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.IterableKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.JodaTimeResolver
import io.github.detomarco.kotlinfixture.resolver.KFunctionResolver
import io.github.detomarco.kotlinfixture.resolver.KNamedPropertyResolver
import io.github.detomarco.kotlinfixture.resolver.KTypeResolver
import io.github.detomarco.kotlinfixture.resolver.LocaleResolver
import io.github.detomarco.kotlinfixture.resolver.MapKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.ObjectResolver
import io.github.detomarco.kotlinfixture.resolver.PrimitiveArrayResolver
import io.github.detomarco.kotlinfixture.resolver.PrimitiveResolver
import io.github.detomarco.kotlinfixture.resolver.Resolver
import io.github.detomarco.kotlinfixture.resolver.SealedClassResolver
import io.github.detomarco.kotlinfixture.resolver.StringResolver
import io.github.detomarco.kotlinfixture.resolver.SubTypeResolver
import io.github.detomarco.kotlinfixture.resolver.TimeResolver
import io.github.detomarco.kotlinfixture.resolver.TupleKTypeResolver
import io.github.detomarco.kotlinfixture.resolver.UriResolver
import io.github.detomarco.kotlinfixture.resolver.UrlResolver
import io.github.detomarco.kotlinfixture.resolver.UuidResolver
import io.github.detomarco.kotlinfixture.toUnmodifiableList
import io.github.detomarco.kotlinfixture.toUnmodifiableMap
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * The [Configuration] for generating the current fixture. This is a combination of all previous configurations.
 * @property repeatCount The length used for lists and maps.
 * @property propertiesRepeatCount Overrides the length used for lists and maps
 * on constructor parameters and mutable properties when generating instances of generic classes.
 * @property properties Overrides for constructor parameters and mutable properties
 *  when generating instances of generic classes.
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
            FileResolver(),
            FormatResolver(),
            CurrencyResolver(),
            LocaleResolver(),

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
