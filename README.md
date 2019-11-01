# Fixture for Kotlin

[![Download](https://api.bintray.com/packages/appmattus/maven/fixture/images/download.svg)](https://bintray.com/appmattus/maven/fixture/_latestVersion)
[![CI status](https://github.com/appmattus/kotlinfixture/workflows/CI/badge.svg)](https://github.com/appmattus/kotlinfixture/actions)
[![Coverage status](https://codecov.io/gh/appmattus/kotlinfixture/branch/master/graph/badge.svg)](https://codecov.io/gh/appmattus/kotlinfixture)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.md)

## Getting started

Include the following dependencies in your `build.gradle.kts` file:

```kotlin
testImplementation("com.appmattus.fixture:fixture:<latest-version>")

// Add for KotlinTest integration
testImplementation("com.appmattus.fixture:fixture-kotlintest:<latest-version>")
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

### Configuration options

The default configuration can be overridden when creating the fixture object or
when creating a particular implementation.

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
    Random.nextInt(1, 5)
}
```

#### subType

Used to always return an instance of a particular subclass for a superclass.

```kotlin
val fixture = kotlinFixture {
    subType<Number, Int>()
}

val alwaysInt = fixture<Number>()

val alwaysFloat = fixture<Number>() {
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

val alwaysOnePointFive = fixture<Number>() {
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
    Random.nextInt(10, 50)
}
```

##### Date and Calendar instances

By default `Date` and `Calendar` instances pick a date within one year either
side of now.

This can be overridden using `factory` which has some built in constructs:

```kotlin
val fixture = kotlinFixture {
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
    property<KotlinClass>("private") { "b" }

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
    property(JavaClass::setMutable) { "d" }

    // Constructor parameters don't typically retain names and so are
    // overridden by a positional 'arg' names:
    property<JavaClass>("arg0") { "e" }
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
val aStaticValue = fixture<Int>() {
    random = Random(seed = 5)
}
```

#### recursionStrategy

When recursion is detected the library will, by default, throw a
`FixtureException` with the details of the circular reference. This strategy can
be changed to instead return `null` for the reference, however, if this results
in an invalid object an exception will still be thrown as the object requested
couldn't be resolved.

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

fixture<String>() {
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

## KotlinTest support

[KotlinTest](https://github.com/kotlintest/kotlintest/) supports
[property testing](https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md#property-based),
but to use it with more than just the few basic types that are built into the
library requires you to create your own custom generators that you then have to
provide.

Including the `fixture-kotlintest` dependency in your project adds extension
functions `assertAll`, `assertNone`, `forAll` and `forNone` to the fixture.
These functions wrap the equivalent functions from KotlinTest while providing
generation of all the classes [KotlinFixture](https://github.com/appmattus/kotlinfixture)
supports. For example:

```kotlin
data class Person(name: String, age: Int)

fixture.assertAll { person1: Person, person2: Person ->
   person1 shouldNotBeSameInstanceAs person2
}
```

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
