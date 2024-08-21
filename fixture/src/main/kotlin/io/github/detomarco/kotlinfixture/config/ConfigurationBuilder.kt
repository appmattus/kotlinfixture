/*
 * Copyright 2020 Appmattus Limited
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

import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.decorator.Decorator
import io.github.detomarco.kotlinfixture.decorator.filter.DefaultFilter
import io.github.detomarco.kotlinfixture.decorator.filter.Filter
import io.github.detomarco.kotlinfixture.resolver.Resolver
import io.github.detomarco.kotlinfixture.toUnmodifiableList
import io.github.detomarco.kotlinfixture.toUnmodifiableMap
import io.github.detomarco.kotlinfixture.typeOf
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

/**
 * Builder of [Configuration]. When `configuration` is supplied it is built upon allowing the various options to be set and overridden.
 */
@ConfigurationDsl
@Suppress("TooManyFunctions")
class ConfigurationBuilder(configuration: Configuration = Configuration()) {

    /**
     * Add and remove [Decorator] to wrap the resolver chain.
     *
     * [Decorator] allow you to intercept and modify the [Resolver] chain
     */
    var decorators: MutableList<Decorator> = configuration.decorators.toMutableList()

    /**
     * Add and remove [Resolver] to the resolver chain.
     *
     * We ask each [Resolver] if it handles the current input object, and it returns either a generated fixture or
     * [Unresolved.Unhandled].
     */
    var resolvers: MutableList<Resolver> = configuration.resolvers.toMutableList()

    /**
     * Providing a seeded random
     *
     * By default, we generate unique values between runs using a default Random class. If you want repeatability you can specify a seeded Random instance.
     *
     * ```
     * val fixture = kotlinFixture {
     *     random = Random(seed = 10)
     * }
     *
     * val alwaysTheSame = fixture<Int>()
     * ```
     *
     * NOTE: While you can specify `random` at object creation, this will make the result static i.e. `fixture<Int> { random = Random(seed = 5) }` will always return the same value.
     */
    var random: Random = configuration.random

    private var repeatCount: () -> Int = configuration.repeatCount
    private val propertiesRepeatCount: MutableMap<KClass<*>, MutableMap<String, () -> Int>> =
        configuration.propertiesRepeatCount.mapValues { it.value.toMutableMap() }.toMutableMap()
    private val properties: MutableMap<KClass<*>, MutableMap<String, GeneratorFun>> =
        configuration.properties.mapValues { it.value.toMutableMap() }.toMutableMap()
    private val factories: MutableMap<KType, GeneratorFun> = configuration.factories.toMutableMap()
    private val filters: MutableMap<KType, Filter> = configuration.filters.toMutableMap()
    private val subTypes: MutableMap<KClass<*>, KClass<*>> = configuration.subTypes.toMutableMap()

    internal val strategies: MutableMap<KClass<*>, Any> = configuration.strategies.toMutableMap()

    /**
     * Setting list and map length with `repeatCount`
     *
     * Used to determine the length used for lists and maps. By default, the library generates 5 items.
     *
     * ```
     * val fixture = kotlinFixture {
     *     repeatCount { 3 }
     * }
     *
     * val listOfThreeItems = fixture<List<Int>>() // 10, 81, 3
     * ```
     *
     * `repeatCount` is a factory method so can be used to return lists and maps of different lengths each execution:
     *
     * ```
     * repeatCount {
     *     random.nextInt(1, 5)
     * }
     * ```
     */
    fun repeatCount(generator: () -> Int) {
        repeatCount = generator
    }

    /**
     * Customising list and map length of class properties with property
     *
     * Used to override list and map length of constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: List<String>, private var private: List<String>) {
     *     var member: List<String>? = null
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     repeatCount(KotlinClass::readOnly) { 1 }
     *
     *     // Private constructor parameters are overridden by name:
     *     repeatCount<KotlinClass>("private") { 2 }
     *
     *     // Public member properties overridden by reference:
     *     repeatCount(KotlinClass::member) { 3 }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final List<String> constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     repeatCount(JavaClass::setMutable) { 1 }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     repeatCount<JavaClass>("arg0") { 2 }
     * }
     * ```
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified T> repeatCount(
        propertyName: String,
        noinline generator: () -> Int
    ) =
        repeatCount(T::class, propertyName, generator)

    /**
     * Customising list and map length of class properties with property
     *
     * Used to override list and map length of constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: List<String>, private var private: List<String>) {
     *     var member: List<String>? = null
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     repeatCount(KotlinClass::readOnly) { 1 }
     *
     *     // Private constructor parameters are overridden by name:
     *     repeatCount<KotlinClass>("private") { 2 }
     *
     *     // Public member properties overridden by reference:
     *     repeatCount(KotlinClass::member) { 3 }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final List<String> constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     repeatCount(JavaClass::setMutable) { 1 }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     repeatCount<JavaClass>("arg0") { 2 }
     * }
     * ```
     */
    inline fun <reified T, G> repeatCount(
        property: KProperty1<T, G>,
        noinline generator: () -> Int
    ) {
        // Only allow read only properties in constructor(s)
        if (property !is KMutableProperty1) {
            val constructorParams = T::class.constructors.flatMap {
                it.parameters.map(KParameter::name)
            }

            check(constructorParams.contains(property.name)) {
                "No setter available for ${T::class.qualifiedName}.${property.name}"
            }
        }

        @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
        return repeatCount(T::class, property.name, generator)
    }

    /**
     * Customising list and map length of class properties with property
     *
     * Used to override list and map length of constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: List<String>, private var private: List<String>) {
     *     var member: List<String>? = null
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     repeatCount(KotlinClass::readOnly) { 1 }
     *
     *     // Private constructor parameters are overridden by name:
     *     repeatCount<KotlinClass>("private") { 2 }
     *
     *     // Public member properties overridden by reference:
     *     repeatCount(KotlinClass::member) { 3 }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final List<String> constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override repeatCount when creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     repeatCount(JavaClass::setMutable) { 1 }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     repeatCount<JavaClass>("arg0") { 2 }
     * }
     * ```
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    fun repeatCount(
        function: KFunction<Unit>,
        generator: () -> Int
    ) = repeatCount(
        function.parameters[0].type.classifier as KClass<*>,
        function.name,
        generator
    )

    @Deprecated(
        "Use one of the property(Class::property) { … }, property<Class, Property>(propertyName) { … } or " +
                "property<Property>(Class::function) { … } functions",
        level = DeprecationLevel.ERROR
    )
    fun repeatCount(clazz: KClass<*>, propertyName: String, generator: () -> Int) {
        val classProperties = propertiesRepeatCount.getOrElse(clazz) { mutableMapOf() }
        classProperties[propertyName] = generator

        propertiesRepeatCount[clazz] = classProperties
    }

    /**
     * Customising class generation with factory
     *
     * Used to return the given instance for a particular class using a factory method.
     *
     * ```
     * val fixture = kotlinFixture {
     *     factory<Number> { 41 }
     * }
     *
     * val alwaysFortyOne = fixture<Number>()
     * ```
     *
     * As `factory` is a factory method you can return different values on every execution:
     *
     * ```
     * factory<Number> {
     *     random.nextInt(10, 50)
     * }
     * ```
     *
     * #### Generating values in a `range`
     *
     * `factory` has a built-in `range` function to make it easy to generate values in a range.
     *
     * ```
     * factory<Int> { range(1..10) }
     * ```
     *
     * #### Generating `Date` and `Calendar` values
     *
     * By default, `Date` and `Calendar` instances pick a date within 10 years of 1 Jan 2020.
     *
     * This can be overridden using the built-in constructs `between`, `before` and `after` in your factory definition:
     *
     * ```
     * factory<Date> { between(startDate, endDate) }
     * ```
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified T> factory(noinline generator: Generator<T>.() -> T) =
        factory(typeOf<T>(), generator as GeneratorFun)

    @Deprecated("Use the factory<Class> { … } function", level = DeprecationLevel.ERROR)
    fun factory(type: KType, generator: GeneratorFun) {
        factories[type] = generator
    }

    /**
     * Filtering generated values with `filter`
     *
     * Used to allow generated values to be filtered using standard sequence functions.
     *
     * ```
     * val fixture = kotlinFixture {
     *     filter<Int> {
     *         filter { it % 2 == 0 }
     *     }
     *
     *     // Can be used to return distinct values.
     *     filter<String> {
     *         distinct()
     *     }
     * }
     *
     * val evenNumber = fixture<Int>()
     *
     * val evenNumberLessThan100 = fixture<Int> {
     *     // Builds upon the parent configuration
     *     filter<Int> {
     *         filter { it < 100 }
     *     }
     * }
     * ```
     *
     * WARNING: The sequence can hang indefinitely if the applied operators prevent the generation of new values. For example:
     * - `distinct` will hang if we exhaust all available values. A good practice is to add a `take(count)` which will throw a `NoSuchElementException` if we try to generate more values.
     * - `filter` that can never be fulfilled e.g. `filter { false }`
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified T> filter(noinline mapping: Sequence<T>.() -> Sequence<T>) =
        filter(typeOf<T>(), mapping as Sequence<Any?>.() -> Sequence<Any?>)

    @Deprecated("Use the filter<Class> { … } function", level = DeprecationLevel.ERROR)
    fun filter(type: KType, mapping: Sequence<Any?>.() -> Sequence<Any?>) {
        filters[type] = (filters.getOrElse(type) { DefaultFilter(type) }).map(mapping)
    }

    /**
     * Resolving abstract superclasses to a chosen subclass with `subType`
     *
     * Used to always return an instance of a particular subclass for a superclass.
     *
     * ```
     * val fixture = kotlinFixture {
     *     subType<Number, Int>()
     * }
     *
     * val alwaysInt = fixture<Number>()
     * ```
     */
    @Suppress("DEPRECATION_ERROR")
    inline fun <reified T, reified U : T> subType() = subType(T::class, U::class)

    @Deprecated(
        "Use the subType<Superclass, Subclass>() function instead",
        level = DeprecationLevel.ERROR
    )
    fun subType(superType: KClass<*>, subType: KClass<*>) {
        subTypes[superType] = subType
    }

    /**
     * Customising generation of class properties with property
     *
     * Used to override constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: String, private var private: String) {
     *     var member: String? = null
     * }
     * ```
     *
     * We can override creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     property(KotlinClass::readOnly) { "a" }
     *
     *     // Private constructor parameters are overridden by name:
     *     property<KotlinClass, String>("private") { "b" }
     *
     *     // Public member properties overridden by reference:
     *     property(KotlinClass::member) { "c" }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final String constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     property<String>(JavaClass::setMutable) { "d" }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     property<JavaClass, String>("arg0") { "e" }
     * }
     * ```
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified T, G> property(
        propertyName: String,
        noinline generator: Generator<G>.() -> G
    ) =
        property(T::class, propertyName, generator as GeneratorFun)

    /**
     * Customising generation of class properties with property
     *
     * Used to override constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: String, private var private: String) {
     *     var member: String? = null
     * }
     * ```
     *
     * We can override creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     property(KotlinClass::readOnly) { "a" }
     *
     *     // Private constructor parameters are overridden by name:
     *     property<KotlinClass, String>("private") { "b" }
     *
     *     // Public member properties overridden by reference:
     *     property(KotlinClass::member) { "c" }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final String constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     property<String>(JavaClass::setMutable) { "d" }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     property<JavaClass, String>("arg0") { "e" }
     * }
     * ```
     */
    inline fun <reified T, G> property(
        property: KProperty1<T, G>,
        noinline generator: Generator<G>.() -> G
    ) {
        // Only allow read only properties in constructor(s)
        if (property !is KMutableProperty1) {
            val constructorParams = T::class.constructors.flatMap {
                it.parameters.map(KParameter::name)
            }

            check(constructorParams.contains(property.name)) {
                "No setter available for ${T::class.qualifiedName}.${property.name}"
            }
        }

        @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
        return property(T::class, property.name, generator as GeneratorFun)
    }

    /**
     * Customising generation of class properties with property
     *
     * Used to override constructor parameters or mutable properties when generating instances of generic classes.
     *
     * #### Kotlin class example
     *
     * Given the following Kotlin class:
     *
     * ```
     * class KotlinClass(val readOnly: String, private var private: String) {
     *     var member: String? = null
     * }
     * ```
     *
     * We can override creating an instance of `KotlinClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Public constructor parameters overridden by reference:
     *     property(KotlinClass::readOnly) { "a" }
     *
     *     // Private constructor parameters are overridden by name:
     *     property<KotlinClass, String>("private") { "b" }
     *
     *     // Public member properties overridden by reference:
     *     property(KotlinClass::member) { "c" }
     * }
     * ```
     *
     * #### Java class example
     *
     * Given the following Java class:
     *
     * ```
     * public class JavaClass {
     *     private final String constructor;
     *     private String mutable;
     *
     *     public JavaClass(String constructor) { this.constructor = constructor; }
     *
     *     public void setMutable(String mutable) { this.mutable = mutable; }
     * }
     * ```
     *
     * We can override creating an instance of `JavaClass` as follows:
     *
     * ```
     * val fixture = kotlinFixture {
     *     // Setter overridden by reference:
     *     property<String>(JavaClass::setMutable) { "d" }
     *
     *     // Constructor parameters don't typically retain names and so are
     *     // overridden by a positional 'arg' names:
     *     property<JavaClass, String>("arg0") { "e" }
     * }
     * ```
     */
    @Suppress("UNCHECKED_CAST", "DEPRECATION_ERROR")
    inline fun <reified G> property(
        function: KFunction<Unit>,
        noinline generator: Generator<G>.() -> G
    ) = property(
        function.parameters[0].type.classifier as KClass<*>,
        function.name,
        generator as GeneratorFun
    )

    @Deprecated(
        "Use one of the property(Class::property) { … }, property<Class, Property>(propertyName) { … } or " +
                "property<Property>(Class::function) { … } functions",
        level = DeprecationLevel.ERROR
    )
    fun property(clazz: KClass<*>, propertyName: String, generator: GeneratorFun) {
        val classProperties = properties.getOrElse(clazz) { mutableMapOf() }
        classProperties[propertyName] = generator

        properties[clazz] = classProperties
    }

    fun build() = Configuration(
        repeatCount = repeatCount,
        propertiesRepeatCount = propertiesRepeatCount,
        properties = properties.mapValues { it.value.toUnmodifiableMap() }.toUnmodifiableMap(),
        factories = factories.toUnmodifiableMap(),
        subTypes = subTypes.toUnmodifiableMap(),
        random = random,
        decorators = decorators.toUnmodifiableList(),
        resolvers = resolvers.toUnmodifiableList(),
        strategies = strategies.toUnmodifiableMap(),
        filters = filters.toUnmodifiableMap()
    )
}
