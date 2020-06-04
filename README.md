# Fixture for Kotlin

[![Download](https://api.bintray.com/packages/appmattus/maven/fixture/images/download.svg)](https://bintray.com/appmattus/maven/fixture/_latestVersion)
[![CI status](https://github.com/appmattus/kotlinfixture/workflows/CI/badge.svg)](https://github.com/appmattus/kotlinfixture/actions)
[![Coverage status](https://codecov.io/gh/appmattus/kotlinfixture/branch/master/graph/badge.svg)](https://codecov.io/gh/appmattus/kotlinfixture)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.md)

## Getting started

Include the following dependencies in your `build.gradle.kts` file:

```kotlin
testImplementation("com.appmattus.fixture:fixture:<latest-version>")

// Add for Kotest integration
testImplementation("com.appmattus.fixture:fixture-kotest:<latest-version>")

// Add for Java Faker integration
testImplementation("com.appmattus.fixture:fixture-javafaker:<latest-version>")
```

Simply create a fixture and invoke it with the type to be generated:

```kotlin
val fixture = kotlinFixture()

// Generate a list of strings
val aListOfStrings = fixture<List<String>>()

// Nulls are supported
val sometimesNull = fixture<Int?>()

// Create instances of classes
// Optional parameters will be randomly used or overridden
data class ADataClass(val value: String = "default")
val aClass = fixture<ADataClass>()

// Abstract classes will pick a sub-class at random
// This could be a Byte, Double, Long, Float, Int or Short
val anyNumber = fixture<Number>()

// Pick randomly from a list
val randomStringFromTheList = fixture(listOf("Cat", "Dog", "Horse"))
val anotherRandomIntFromAList = fixture(1..5)
```

You can also generate an infinite sequence of a type, which can then be
filtered:

```kotlin
val fixture = kotlinFixture()

val intSequence = fixture.asSequence<Int>()

// Standard Kotlin sequence functions can then be applied before using
// the sequence through an iterator for access to the next() function.

// For example, you can filter values
val oddIterator = intSequence.filter { it.absoluteValue.rem(2) == 1 }.iterator()
val oddNumber = oddIterator.next()
val anotherOddNumber = oddIterator.next()

// Or, ensure it returns only distinct values
// NOTE: As the sequence is infinite, distinct will hang if no more
// distinct values can be generated
enum class XYZ { X, Y, Z }
val enumIterator = fixture.asSequence<XYZ>().distinct().iterator()
val aDistinctValue = enumIterator.next()
val anotherDistinctValue = enumIterator.next()
```

### Configuration options

The default configuration can be overridden when creating the fixture object or
when creating a particular implementation.

It is possible to create a new fixture based on an existing one, which allows
the addition of configuration changes:

```kotlin
val baseFixture = kotlinFixture {
    factory<Int> { 3 }
}

val fixture = baseFixture.new {
    factory<Long> { 100L }
}

// Prints 100
println(fixture<Long>())
// Prints 3
println(fixture<Int>())
```

#### repeatCount

Used to determine the length used for lists and maps.

```kotlin
val fixture = kotlinFixture {
    repeatCount { 3 }
}

val listOfThreeItems = fixture<List<String>>()

val listOfSevenItems = fixture<List<String>> {
    // Overrides the parent configuration
    repeatCount { 7 }
}
```

`repeatCount` is a factory method so can be used to return lists and maps of
different lengths each execution:

```kotlin
repeatCount {
    random.nextInt(1, 5)
}
```

#### subType

Used to always return an instance of a particular subclass for a superclass.

```kotlin
val fixture = kotlinFixture {
    subType<Number, Int>()
}

val alwaysInt = fixture<Number>()

val alwaysFloat = fixture<Number> {
    // Overrides the parent configuration
    subType<Number, Float>()
}
```

#### factory

Used to return the given instance for a particular class using a factory
method.

```kotlin
val fixture = kotlinFixture {
    factory<Number> {
        41
    }
}

val alwaysFortyOne = fixture<Number>()

val alwaysOnePointFive = fixture<Number> {
    // Overrides the parent configuration
    factory<Number> {
        1.5
    }
}
```

As `factory` is a factory method you can return different values on every
execution:

```kotlin
factory<Number> {
    random.nextInt(10, 50)
}
```

##### Date and Calendar instances

By default `Date` and `Calendar` instances pick a date within 10 years of
1 Jan 2020.

This can be overridden using `factory` which has some built in constructs:

```kotlin
val fixture = kotlinFixture {
    // Generate using ranges (and iterables)
    factory<Int> { range(1..10) }

    // Generate between two dates
    factory<Date> { between(startDate, endDate) }
}

val betweenTwoDates = fixture<Date>()

// You can also override at instance creation

val pastDate = fixture<Date> {
    factory<Date> { before(Date()) }
}

val futureDate = fixture<Date> {
    factory<Date> { after(Date()) }
}
```

#### filter

Used to allow generated values to be filtered using standard sequence
functions.

```kotlin
val fixture = kotlinFixture {
    filter<Int> {
        filter { it % 2 == 0 }
    }

    // Can be used to return distinct values.
    // NOTE: As the sequence is infinite, distinct will hang if no more
    // distinct values can be generated
    filter<String> {
        distinct()
    }
}

val evenNumber = fixture<Int>()

val evenNumberLessThan100 = fixture<Int> {
    // Overrides the parent configuration
    filter<Int> {
        filter { it < 100 }
    }
}
```

#### property

Used to override constructor parameters or mutable properties when generating
instances of generic classes.

Given the following Kotlin class:

```kotlin
class KotlinClass(val readOnly: String, private var private: String) {
    var member: String? = null
}
```

We can override creating an instance of `KotlinClass` as follows:

```kotlin
val fixture = kotlinFixture {
    // Public constructor parameters overridden by reference:
    property(KotlinClass::readOnly) { "a" }

    // Private constructor parameters are overridden by name:
    property<KotlinClass, String>("private") { "b" }

    // Public member properties overridden by reference:
    property(KotlinClass::member) { "c" }
}
```

Given the following Java class:

```java
public class JavaClass {
    private final String constructor;
    private String mutable;

    public JavaClass(String constructor) { this.constructor = constructor; }

    public void setMutable(String mutable) { this.mutable = mutable; }
}
```

We can override creating an instance of `JavaClass` as follows:

```kotlin
val fixture = kotlinFixture {
    // Setter overridden by reference:
    property<String>(JavaClass::setMutable) { "d" }

    // Constructor parameters don't typically retain names and so are
    // overridden by a positional 'arg' names:
    property<JavaClass, String>("arg0") { "e" }
}
```

#### random

By default a `Random` class is used that will generate unique values between
runs. If you want repeatability you can specify a seeded `Random` instance.

```kotlin
val fixture = kotlinFixture {
    random = Random(seed = 10)
}

val alwaysTheSame = fixture<Int>()

// Can be specified on creation, but makes the result static
val aStaticValue = fixture<Int> {
    random = Random(seed = 5)
}
```

#### nullabilityStrategy

By default when the library comes across a nullable type, such as `String?` it
will randomly return a value or null. This can be overridden by setting a
nullability strategy.

```kotlin
val fixture = kotlinFixture {
    // All nullable types will be populated with a value
    nullabilityStrategy(NeverNullStrategy)
}

// You can also override at instance creation

fixture<AnObject> {
    // All nullable types will be populated with null
    nullabilityStrategy(AlwaysNullStrategy)
}
```

It is also possible to define and implement your own nullability strategy by
implementing `NullabilityStrategy` and applying it as above.

#### optionalStrategy

By default when the library comes across an optional type, such as
`value: String = "default"` it will randomly return that default value
or a generated value. This can be overridden by setting an optional
strategy.

```kotlin
val fixture = kotlinFixture {
    // All optionals will be populated with generated values
    optionalStrategy(NeverOptionalStrategy)
}

// You can also override at instance creation

fixture<AnObject> {
    // All optionals will be populated with their default value
    optionalStrategy(AlwaysOptionalStrategy) {
        // You can override the strategy for a particular class
        classOverride<AnotherObject>(NeverOptionalStrategy)

        // You can override the strategy for a property of a class
        propertyOverride(AnotherObject::property, RandomlyOptionalStrategy)
    }
}
```

#### recursionStrategy

When recursion is detected the library will, by default, throw an
`UnsupportedOperationException` with the details of the circular reference. This
strategy can be changed to instead return `null` for the reference, however, if
this results in an invalid object an exception will still be thrown as the
object requested couldn't be resolved.

```kotlin
val fixture = kotlinFixture {
    recursionStrategy(NullRecursionStrategy)
}

// You can also override at instance creation

fixture<AnObject> {
    recursionStrategy(NullRecursionStrategy)
}
```

It is also possible to define and implement your own recursion strategy by
implementing `RecursionStrategy` and applying it as above.

#### loggingStrategy

A basic logger can be applied using the built in `SysOutLoggingStrategy`. It is
also possible to define and implement your own logging strategy by implementing
`LoggingStrategy` and applying it as below.

```kotlin
val fixture = kotlinFixture {
    loggingStrategy(SysOutLoggingStrategy)
}

fixture<String> {
    // You can also override at instance creation
    loggingStrategy(SysOutLoggingStrategy)
}
```

This outputs:

```text
ktype kotlin.String →
    class kotlin.String →
        Success(5878ec34-c30f-40c7-ad52-c15a39b44ac1)
    Success(5878ec34-c30f-40c7-ad52-c15a39b44ac1)
```

## Kotest support

[Kotest](https://github.com/kotest/kotest/) supports
[property testing](https://github.com/kotest/kotest/blob/master/doc/reference.md#property-based-testing-),
but to use it with more than just the few basic types that are built
into the library requires you to create your own custom generators that
you then have to provide.

Including the `fixture-kotest` dependency in your project adds extension
functions `checkAll` and `forAll` to the fixture. These
functions wrap the equivalent functions from Kotest while providing
generation of all the classes
[KotlinFixture](https://github.com/appmattus/kotlinfixture) supports.
For example:

```kotlin
data class Person(name: String, age: Int)

fixture.checkAll { person1: Person, person2: Person ->
   person1 shouldNotBeSameInstanceAs person2
}
```

## Java Faker support

The [Java Faker](http://dius.github.io/java-faker/) library generates
fake data, useful if you need to generate objects with pretty data.

Including the `fixture-javafaker` dependency in your project adds a
`javaFakerStrategy` which will use
[Java Faker](http://dius.github.io/java-faker/) to populate named
properties such as `name`, `city` and `phoneNumber`. A full list of
supported properties and how they map to
[Java Faker](http://dius.github.io/java-faker/) can be found in
[JavaFakerConfiguration](fixture-javafaker/src/main/kotlin/com/appmattus/kotlinfixture/decorator/fake/javafaker/JavaFakerConfiguration.kt).

Additionally, the `javaFakerStrategy` function allows you to override
faker settings such as `locale`.

```kotlin

val fixture = kotlinFixture {
    javaFakerStrategy()
}

data class Person(val name: String, val age: Long)

println(fixture<Person>()) // Person(name=Keneth Bartoletti, age=54)
```

### Regex to String generation

The module also introduces the ability to generate a random string from
a Regex, with no need to enable the faker functionality:

```kotlin
        data class DataClass(val index: String, val value: String)

        val indexRegex = "[a-z][0-9]".toRegex()
        val valueRegex = "[A-Z]{3}".toRegex()
        
        val fixture = kotlinFixture {
            factory<String> { regexify(indexRegex) }

            property(DataClass::value) { regexify(valueRegex) }
        }

        println(fixture<DataClass>()) // DataClass(index=m3, value=CGJ)
```
s
## Contributing

Please fork this repository and contribute back using
[pull requests](https://github.com/appmattus/kotlinfixture/pulls).

All contributions, large or small, major features, bug fixes, additional
language translations, unit/integration tests are welcomed.

## License

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.md)

Copyright 2019 Appmattus Limited

Licensed under the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License. You may obtain
a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
