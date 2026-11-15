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

package com.appmattus.kotlinfixture.decorator.fake.datafaker

import com.appmattus.kotlinfixture.decorator.fake.datafaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.datafaker.option.IpAddress
import com.appmattus.kotlinfixture.decorator.fake.datafaker.option.Password
import com.appmattus.kotlinfixture.decorator.fake.datafaker.option.Temperature
import com.appmattus.kotlinfixture.decorator.fake.datafaker.option.UserAgent
import net.datafaker.Faker
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DataFakerConfigurationBuilderTest {

    @Test
    fun `can override creditCard`() {
        val configuration =
            DataFakerConfigurationBuilder(DataFakerConfiguration(creditCard = CreditCard.AmericanExpress)).apply {
                creditCard = CreditCard.Visa
            }.build()

        assertEquals(CreditCard.Visa, configuration.creditCard)
    }

    @Test
    fun `can override ipAddress`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(ipAddress = IpAddress.V4)).apply {
            ipAddress = IpAddress.V6
        }.build()

        assertEquals(IpAddress.V6, configuration.ipAddress)
    }

    @Test
    fun `can override isbn10Separator when true`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn10Separator = true)).apply {
            isbn10Separator = false
        }.build()

        assertEquals(false, configuration.isbn10Separator)
    }

    @Test
    fun `can override isbn10Separator when false`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn10Separator = false)).apply {
            isbn10Separator = true
        }.build()

        assertEquals(true, configuration.isbn10Separator)
    }

    @Test
    fun `can override isbn13Separator when true`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn13Separator = true)).apply {
            isbn13Separator = false
        }.build()

        assertEquals(false, configuration.isbn13Separator)
    }

    @Test
    fun `can override isbn13Separator when false`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(isbn13Separator = false)).apply {
            isbn13Separator = true
        }.build()

        assertEquals(true, configuration.isbn13Separator)
    }

    @Test
    fun `can override locale`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(locale = Locale.FRENCH)).apply {
            locale = Locale.GERMAN
        }.build()

        assertEquals(Locale.GERMAN, configuration.locale)
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

        assertEquals(3, configuration.password.minimumLength)
        assertEquals(4, configuration.password.maximumLength)
        assertEquals(true, configuration.password.includeUppercase)
        assertEquals(true, configuration.password.includeSpecial)
        assertEquals(true, configuration.password.includeDigit)
    }

    @Test
    fun `can override temperature`() {
        val configuration =
            DataFakerConfigurationBuilder(DataFakerConfiguration(temperature = Temperature.Celsius)).apply {
                temperature = Temperature.Fahrenheit
            }.build()

        assertEquals(Temperature.Fahrenheit, configuration.temperature)
    }

    @Test
    fun `can override userAgent`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration(userAgent = UserAgent.Chrome)).apply {
            userAgent = UserAgent.Safari
        }.build()

        assertEquals(UserAgent.Safari, configuration.userAgent)
    }

    @Test
    fun `can override properties using putProperty(String,Lambda)`() {
        val original: Faker.(DataFakerConfiguration) -> Any? = { "original" }

        @Suppress("UNCHECKED_CAST")
        val configuration = DataFakerConfigurationBuilder(
            DataFakerConfiguration(properties = mapOf("name" to original))
        ).apply {
            putProperty("name") {
                "replacement"
            }
        }.build()

        assertEquals(
            "replacement",
            configuration.properties.getValue("name").invoke(Faker(), configuration)
        )
    }

    @Test
    fun `can override properties using removeProperty(String)`() {
        val original: Faker.(DataFakerConfiguration) -> Any? = { "original" }

        @Suppress("UNCHECKED_CAST")
        val configuration = DataFakerConfigurationBuilder(
            DataFakerConfiguration(properties = mapOf("name" to original))
        ).apply {
            removeProperty("name")
        }.build()

        assertNull(
            configuration.properties["name"]
        )
    }

    @Test
    fun `properties is immutable`() {
        val configuration = DataFakerConfigurationBuilder(DataFakerConfiguration()).build()
        val function: Faker.(DataFakerConfiguration) -> Any? = { }

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST", "ReplacePutWithAssignment")
            (configuration.properties as MutableMap<String, Faker.(DataFakerConfiguration) -> Any?>).put(
                "string",
                function
            )
        }
    }
}
