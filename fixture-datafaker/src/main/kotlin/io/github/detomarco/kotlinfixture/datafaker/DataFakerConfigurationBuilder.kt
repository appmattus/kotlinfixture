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

package io.github.detomarco.kotlinfixture.datafaker

import io.github.detomarco.kotlinfixture.datafaker.option.IpAddress
import io.github.detomarco.kotlinfixture.datafaker.option.Temperature
import io.github.detomarco.kotlinfixture.datafaker.option.UserAgent
import io.github.detomarco.kotlinfixture.toUnmodifiableMap
import net.datafaker.Faker
import java.util.Locale

/**
 * Builder of [DataFakerConfiguration].
 */
class DataFakerConfigurationBuilder internal constructor(dataFakerConfiguration: DataFakerConfiguration) {

    /**
     * Generate `creditCard` properties in the style of the selected credit card company. See [CreditCard] for options.
     * Default: A credit card number from any company.
     */
    var creditCard = dataFakerConfiguration.creditCard

    /**
     * Generate `ipAddress` properties using [IpAddress.V4] or [IpAddress.V6] style.
     * Default: [IpAddress.V4]
     */
    var ipAddress = dataFakerConfiguration.ipAddress

    /**
     * Generate `isbn10` properties with or without separators.
     * Default: false - without separators.
     */
    var isbn10Separator = dataFakerConfiguration.isbn10Separator

    /**
     * Generate `isbn13` properties with or without separators.
     * Default: false - without separators.
     */
    var isbn13Separator = dataFakerConfiguration.isbn13Separator

    /**
     * The [Locale] used to generate fake data.
     * Default: [Locale.ENGLISH]
     */
    var locale = dataFakerConfiguration.locale

    /**
     * Generate `password` properties using digits, uppercase, special characters or a combination, and a minimum and
     * maximum length.
     * Default: digits, uppercase and special characters between 8 and 16 characters in length.
     */
    var password = dataFakerConfiguration.password

    /**
     * Generate `temperature` properties using [Temperature.Celsius] or [Temperature.Fahrenheit].
     * Default: [Temperature.Celsius]
     */
    var temperature = dataFakerConfiguration.temperature

    /**
     * Generate `userAgent` properties in the style of the selected browser. See [UserAgent] for options.
     * Default: [UserAgent.Any], simulating any browser.
     */
    var userAgent = dataFakerConfiguration.userAgent

    private val properties = dataFakerConfiguration.properties.toMutableMap()

    /**
     * Remove the [Java Faker](https://github.com/DiUS/java-faker) generator for [propertyName].
     */
    fun removeProperty(propertyName: String) {
        properties.remove(propertyName)
    }

    /**
     * Map a [Java Faker](https://github.com/DiUS/java-faker) generator to a [propertyName].
     */
    fun putProperty(propertyName: String, fakeGenerator: Faker.(DataFakerConfiguration) -> Any?) {
        properties[propertyName] = fakeGenerator
    }

    internal fun build() = DataFakerConfiguration(
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
