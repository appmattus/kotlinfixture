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

import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.Temperature
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.UserAgent
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.IpAddress
import com.appmattus.kotlinfixture.toUnmodifiableMap
import com.github.javafaker.Faker
import java.util.Locale

class JavaFakerConfigurationBuilder(javaFakerConfiguration: JavaFakerConfiguration) {

    /**
     * Generate `creditCard` properties in the style of the selected credit card company. See [CreditCard] for options.
     * Default: A credit card number from any company.
     */
    var creditCard = javaFakerConfiguration.creditCard

    /**
     * Generate `ipAddress` properties using [IpAddress.V4] or [IpAddress.V6] style.
     * Default: [IpAddress.V4]
     */
    var ipAddress = javaFakerConfiguration.ipAddress

    /**
     * Generate `isbn10` properties with or without separators.
     * Default: false - without separators.
     */
    var isbn10Separator = javaFakerConfiguration.isbn10Separator

    /**
     * Generate `isbn13` properties with or without separators.
     * Default: false - without separators.
     */
    var isbn13Separator = javaFakerConfiguration.isbn13Separator

    /**
     * The [Locale] used to generate fake data.
     * Default: [Locale.ENGLISH]
     */
    var locale = javaFakerConfiguration.locale

    /**
     * Generate `password` properties using digits, uppercase, special characters or a combination, and a minimum and
     * maximum length.
     * Default: digits, uppercase and special characters between 8 and 16 characters in length.
     */
    var password = javaFakerConfiguration.password

    /**
     * Generate `temperature` properties using [Temperature.Celsius] or [Temperature.Fahrenheit].
     * Default: [Temperature.Celsius]
     */
    var temperature = javaFakerConfiguration.temperature

    /**
     * Generate `userAgent` properties in the style of the selected browser. See [UserAgent] for options.
     * Default: [UserAgent.Any], simulating any browser.
     */
    var userAgent = javaFakerConfiguration.userAgent

    private val properties = javaFakerConfiguration.properties.toMutableMap()

    fun removeProperty(propertyName: String) {
        properties.remove(propertyName)
    }

    fun putProperty(propertyName: String, fakeGenerator: Faker.(JavaFakerConfiguration) -> Any?) {
        properties[propertyName] = fakeGenerator
    }

    internal fun build() = JavaFakerConfiguration(
        creditCard = creditCard,
        ipAddress = ipAddress,
        isbn10Separator = isbn10Separator,
        isbn13Separator = isbn13Separator,
        locale = locale,
        password = password,
        temperature = temperature,
        userAgent = userAgent,
        properties = properties.toUnmodifiableMap()
    )
}
