# Fixture for Kotlin

[![Download](https://api.bintray.com/packages/appmattus/maven/fixture/images/download.svg)](https://bintray.com/appmattus/maven/fixture/_latestVersion)
[![CI status](https://github.com/appmattus/kotlinfixture/workflows/CI/badge.svg)](https://github.com/appmattus/kotlinfixture/actions)
[![Coverage status](https://codecov.io/gh/appmattus/kotlinfixture/branch/master/graph/badge.svg)](https://codecov.io/gh/appmattus/kotlinfixture)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.md)

## Getting started

Include the following dependencies in your build.gradle.kts file:

```kotlin
implementation("com.appmattus.fixture:fixture:<latest-version>")
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

The default configuration can be overridden when creating the fixture
object or when creating a particular implementation.

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

repeatCount is a factory method so can be used to return lists and maps
of different lengths each execution:

```kotlin
repeatCount {
    Random.nextInt(1, 5)
}
```

#### subType

Used to always return an instance of a particular subclass for a
superclass.

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

#### instance

Used to return the given instance for a particular class.

```kotlin
val fixture = kotlinFixture {
    instance<Number> {
        41
    }
}

val alwaysFortyOne = fixture<Number>()

val alwaysOnePointFive = fixture<Number>() {
    // Overrides the parent configuration
    instance<Number> {
        1.5
    }
}
```

instance is a factory method so can be used to return different values
on every execution:

```kotlin
instance<Number> {
    Random.nextInt(10, 50)
}
```

#### dateSpecification

By default Date and Calendar instances pick a date within one year
either side of now.

```kotlin
val fixture = kotlinFixture {
    // Generate between two dates
    dateSpecification = DateSpecification.Between(startDate, endDate)
}

val betweenTwoDates = fixture<Date>()

// You can also override at instance creation

val pastDate = fixture<Date> {
    dateSpecification = DateSpecification.Before(Date())
}

val futureDate = fixture<Date> {
    dateSpecification = DateSpecification.After(Date())
}
```

#### random

By default a Random class is used that will generate unique values
between runs. If you want repeatability you can specify a seeded Random
instance.

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
