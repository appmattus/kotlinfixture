/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture.decorator.fake.kotlinfaker

import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.IpAddress
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.UserAgent
import com.appmattus.kotlinfixture.toUnmodifiableMap
import io.github.serpro69.kfaker.Faker
import java.util.Locale

/**
 * Builder of [KotlinFakerConfiguration].
 */
class KotlinFakerConfigurationBuilder internal constructor(kotlinFakerConfiguration: KotlinFakerConfiguration) {

    /**
     * Generate `creditCard` properties in the style of the selected credit card company. See [CreditCard] for options.
     * Default: A credit card number from any company.
     */
    var creditCard = kotlinFakerConfiguration.creditCard

    /**
     * Generate `ipAddress` properties using [IpAddress.V4] or [IpAddress.V6] style.
     * Default: [IpAddress.V4]
     */
    var ipAddress = kotlinFakerConfiguration.ipAddress

    /**
     * The [Locale] used to generate fake data, passed to Kotlin Faker as its language tag, for example `en-GB`.
     * Kotlin Faker throws an [IllegalArgumentException] when generating data for a locale it has no dictionary for.
     * Default: [Locale.ENGLISH]
     */
    var locale = kotlinFakerConfiguration.locale

    /**
     * Generate `userAgent` properties in the style of the selected browser. See [UserAgent] for options.
     * Default: [UserAgent.Any], simulating any browser.
     */
    var userAgent = kotlinFakerConfiguration.userAgent

    private val properties = kotlinFakerConfiguration.properties.toMutableMap()

    /**
     * Remove the [Kotlin Faker](https://github.com/serpro69/kotlin-faker) generator for [propertyName].
     */
    fun removeProperty(propertyName: String) {
        properties.remove(propertyName)
    }

    /**
     * Map a [Kotlin Faker](https://github.com/serpro69/kotlin-faker) generator to a [propertyName].
     */
    fun putProperty(propertyName: String, fakeGenerator: Faker.(KotlinFakerConfiguration) -> Any?) {
        properties[propertyName] = fakeGenerator
    }

    internal fun build() = KotlinFakerConfiguration(
        creditCard = creditCard,
        ipAddress = ipAddress,
        locale = locale,
        userAgent = userAgent,
        properties = properties.toUnmodifiableMap()
    )
}
