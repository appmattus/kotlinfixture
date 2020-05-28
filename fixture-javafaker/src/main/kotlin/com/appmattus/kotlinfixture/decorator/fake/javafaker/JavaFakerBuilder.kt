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

package com.appmattus.kotlinfixture.decorator.fake.javafaker

import com.appmattus.kotlinfixture.toUnmodifiableMap
import com.github.javafaker.Faker

class JavaFakerBuilder(javaFakerConfiguration: JavaFakerConfiguration) {

    var creditCard = javaFakerConfiguration.creditCard
    var ipAddress = javaFakerConfiguration.ipAddress
    var isbn10Separator = javaFakerConfiguration.isbn10Separator
    var isbn13Separator = javaFakerConfiguration.isbn13Separator
    var locale = javaFakerConfiguration.locale
    var password = javaFakerConfiguration.password
    var temperature = javaFakerConfiguration.temperature
    var userAgent = javaFakerConfiguration.userAgent

    private val map = javaFakerConfiguration.map.toMutableMap()

    fun remove(name: String) {
        map.remove(name)
    }

    fun put(name: String, fakeGenerator: Faker.(JavaFakerConfiguration) -> Any?) {
        map[name] = fakeGenerator
    }

    fun build() = JavaFakerConfiguration(
        creditCard = creditCard,
        ipAddress = ipAddress,
        isbn10Separator = isbn10Separator,
        isbn13Separator = isbn13Separator,
        locale = locale,
        password = password,
        temperature = temperature,
        userAgent = userAgent,
        map = map.toUnmodifiableMap()
    )
}
