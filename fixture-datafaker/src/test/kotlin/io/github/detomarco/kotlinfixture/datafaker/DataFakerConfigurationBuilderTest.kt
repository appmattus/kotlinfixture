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

import io.github.detomarco.kotlinfixture.datafaker.option.CreditCard
import io.github.detomarco.kotlinfixture.datafaker.option.IpAddress
import io.github.detomarco.kotlinfixture.datafaker.option.Password
import io.github.detomarco.kotlinfixture.datafaker.option.Temperature
import io.github.detomarco.kotlinfixture.datafaker.option.UserAgent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import java.util.Locale

class DataFakerConfigurationBuilderTest {

    @Test
    fun `can override creditCard`() {
        val configuration =
            DataFakerConfigurationBuilder(DataFakerConfiguration(creditCard = CreditCard.AmericanExpress)).apply {
                creditCard = CreditCard.Visa
            }.build()

        configuration.creditCard shouldBe CreditCard.Visa
        configuration.creditCard shouldBe configuration.creditCard
    }

    @Test
    fun `can override ipAddress`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(ipAddress = IpAddress.V4)).apply {
            ipAddress = IpAddress.V6
        }.build()

        configuration.ipAddress shouldBe IpAddress.V6
    }

    @Test
    fun `can override isbn10Separator when true`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn10Separator = true)).apply {
            isbn10Separator = false
        }.build()

        configuration.isbn10Separator.shouldBeFalse()
    }

    @Test
    fun `can override isbn10Separator when false`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn10Separator = false)).apply {
            isbn10Separator = true
        }.build()

        configuration.isbn10Separator.shouldBeTrue()
    }

    @Test
    fun `can override isbn13Separator when true`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn13Separator = true)).apply {
            isbn13Separator = false
        }.build()

        configuration.isbn13Separator.shouldBeFalse()
    }

    @Test
    fun `can override isbn13Separator when false`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn13Separator = false)).apply {
            isbn13Separator = true
        }.build()

        configuration.isbn13Separator.shouldBeTrue()
    }

    @Test
    fun `can override locale`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(locale = Locale.FRENCH)).apply {
            locale = Locale.GERMAN
        }.build()

        configuration.locale shouldBe Locale.GERMAN
    }

    @Test
    fun `can override password`() {
        val configuration = DataFakerConfigurationBuilder(
            DataFakerConfiguration(
                password = Password(
                    minimumLength = 1,
                    maximumLength = 2,
                    includeUppercase = false,
                    includeSpecial = false,
                    includeDigit = false
                )
            )
        ).apply {
            password = Password(
                minimumLength = 3,
                maximumLength = 4,
                includeUppercase = true,
                includeSpecial = true,
                includeDigit = true
            )
        }.build()

        configuration.password.minimumLength shouldBe 3
        configuration.password.maximumLength shouldBe 4
        configuration.password.includeUppercase shouldBe true
        configuration.password.includeSpecial shouldBe true
        configuration.password.includeDigit shouldBe true
    }

    @Test
    fun `can override temperature`() {
        val configuration =
            DataFakerConfigurationBuilder(DataFakerConfiguration(temperature = Temperature.Celsius)).apply {
                temperature = Temperature.Fahrenheit
            }.build()

        configuration.temperature shouldBe Temperature.Fahrenheit
    }

    @Test
    fun `can override userAgent`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(userAgent = UserAgent.Chrome)).apply {
            userAgent = UserAgent.Safari
        }.build()

        configuration.userAgent shouldBe UserAgent.Safari
    }

    @Test
    fun `can override properties using putProperty(String,Lambda)`() {
        val original: Faker.(DataFakerConfiguration) -> Any? = { "original" }

        val configuration = DataFakerConfigurationBuilder(
            DataFakerConfiguration(properties = mapOf("name" to original))
        ).apply {
            putProperty("name") {
                "replacement"
            }
        }.build()

        configuration.properties.getValue("name").invoke(Faker(), configuration) shouldBe "replacement"
    }

    @Test
    fun `can override properties using removeProperty(String)`() {
        val original: Faker.(DataFakerConfiguration) -> Any? = { "original" }

        val configuration = DataFakerConfigurationBuilder(
            DataFakerConfiguration(properties = mapOf("name" to original))
        ).apply {
            removeProperty("name")
        }.build()

        configuration.properties["name"].shouldBeNull()
    }

    @Test
    fun `properties is immutable`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration()).build()
        val function: Faker.(DataFakerConfiguration) -> Any? = { }

        shouldThrow<UnsupportedOperationException> {
            (configuration.properties as MutableMap<String, Faker.(DataFakerConfiguration) -> Any?>).put(
                "string",
                function
            )
        }
    }
}
