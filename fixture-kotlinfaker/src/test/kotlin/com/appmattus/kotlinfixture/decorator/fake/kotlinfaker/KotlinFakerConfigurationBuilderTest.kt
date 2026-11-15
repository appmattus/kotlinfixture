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
import io.github.serpro69.kfaker.Faker
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class KotlinFakerConfigurationBuilderTest {

    @Test
    fun `can override creditCard`() {
        val configuration =
            KotlinFakerConfigurationBuilder(KotlinFakerConfiguration(creditCard = CreditCard.AmericanExpress)).apply {
                creditCard = CreditCard.Visa
            }.build()

        assertEquals(CreditCard.Visa, configuration.creditCard)
    }

    @Test
    fun `can override ipAddress`() {
        val configuration = KotlinFakerConfigurationBuilder(KotlinFakerConfiguration(ipAddress = IpAddress.V4)).apply {
            ipAddress = IpAddress.V6
        }.build()

        assertEquals(IpAddress.V6, configuration.ipAddress)
    }

    @Test
    fun `can override locale`() {
        val configuration = KotlinFakerConfigurationBuilder(KotlinFakerConfiguration(locale = Locale.FRENCH)).apply {
            locale = Locale.GERMAN
        }.build()

        assertEquals(Locale.GERMAN, configuration.locale)
    }

    @Test
    fun `can override userAgent`() {
        val configuration =
            KotlinFakerConfigurationBuilder(KotlinFakerConfiguration(userAgent = UserAgent.Chrome)).apply {
                userAgent = UserAgent.Safari
            }.build()

        assertEquals(UserAgent.Safari, configuration.userAgent)
    }

    @Test
    fun `can override properties using putProperty(String,Lambda)`() {
        val original: Faker.(KotlinFakerConfiguration) -> Any? = { "original" }

        val configuration = KotlinFakerConfigurationBuilder(
            KotlinFakerConfiguration(properties = mapOf("name" to original))
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
        val original: Faker.(KotlinFakerConfiguration) -> Any? = { "original" }

        val configuration = KotlinFakerConfigurationBuilder(
            KotlinFakerConfiguration(properties = mapOf("name" to original))
        ).apply {
            removeProperty("name")
        }.build()

        assertNull(
            configuration.properties["name"]
        )
    }

    @Test
    fun `properties is immutable`() {
        val configuration = KotlinFakerConfigurationBuilder(KotlinFakerConfiguration()).build()
        val function: Faker.(KotlinFakerConfiguration) -> Any? = { }

        assertFailsWith<UnsupportedOperationException> {
            @Suppress("UNCHECKED_CAST", "ReplacePutWithAssignment")
            (configuration.properties as MutableMap<String, Faker.(KotlinFakerConfiguration) -> Any?>).put(
                "string",
                function
            )
        }
    }
}
