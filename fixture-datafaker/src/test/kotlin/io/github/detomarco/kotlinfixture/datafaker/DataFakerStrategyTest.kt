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

import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.datafaker.option.CreditCard
import io.github.detomarco.kotlinfixture.datafaker.option.IpAddress
import io.github.detomarco.kotlinfixture.datafaker.option.Password
import io.github.detomarco.kotlinfixture.kotlinFixture
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.Locale
import java.util.UUID

class DataFakerStrategyTest {

    data class Person(
        val firstName: String,
        val creditCard: String,
        val ipAddress: String,
        val isbn10: String,
        val isbn13: String,
        val countryCode: String,
        val password: String,
        val miscellaneous: String
    )

    @Test
    fun `firstName property formatted as UUID when no strategy defined`() {
        val fixture = kotlinFixture()

        fixture<Person>().firstName.isUUID().shouldBeTrue()
    }

    @Test
    fun `firstName property formatted by dataFakerStrategy is not a UUID`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        fixture<Person>().firstName.isUUID().shouldBeFalse()
    }

    @Test
    fun `firstName property formatted by dataFakerStrategy is random`() {
        val fixture = kotlinFixture()

        assertIsRandom {
            fixture<Person>().firstName
        }
    }

    @Test
    fun `miscellaneous property not formatted by dataFakerStrategy is a UUID`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        fixture<Person>().miscellaneous.isUUID().shouldBeTrue()
    }

    @Test
    fun `creditCard is any card type by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().creditCard.isAmex()
        }
    }

    @Test
    fun `creditCard can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                creditCard = CreditCard.AmericanExpress
            }
        }

        fixture<Person>().creditCard.isAmex().shouldBeTrue()
    }

    @Test
    @Disabled
    fun `creditCard can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                creditCard = CreditCard.AmericanExpress
            }
        }

        val creditCard = fixture<Person> {
            dataFakerStrategy {
                creditCard = CreditCard.JCB
            }
        }.creditCard

        creditCard.isJcb().shouldBeTrue()
    }

    @Test
    fun `ipAddress is v4 by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        fixture<Person>().ipAddress.isIpV4().shouldBeTrue()
    }

    @Test
    fun `ipAddress can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                ipAddress = IpAddress.V6
            }
        }

        fixture<Person>().ipAddress.isIpV6().shouldBeTrue()
    }

    @Test
    fun `ipAddress can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                ipAddress = IpAddress.V6
            }
        }

        fixture<Person> {
            dataFakerStrategy {
                ipAddress = IpAddress.V4
            }
        }.ipAddress.isIpV4().shouldBeTrue()
    }

    @Test
    fun `isbn10Separator is false by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        fixture<Person>().isbn10.isbnHasSeparator().shouldBeFalse()
    }

    @Test
    fun `isbn10Separator can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                isbn10Separator = true
            }
        }

        fixture<Person>().isbn10.isbnHasSeparator().shouldBeTrue()
    }

    @Test
    fun `isbn10Separator can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                isbn10Separator = true
            }
        }

        fixture<Person> {
            dataFakerStrategy {
                isbn10Separator = false
            }
        }.isbn10.isbnHasSeparator().shouldBeFalse()
    }

    @Test
    fun `isbn13Separator is false by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        fixture<Person>().isbn13.isbnHasSeparator().shouldBeFalse()
    }

    @Test
    fun `isbn13Separator can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                isbn13Separator = true
            }
        }

        fixture<Person>().isbn13.isbnHasSeparator().shouldBeTrue()
    }

    @Test
    fun `isbn13Separator can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                isbn13Separator = true
            }
        }

        fixture<Person> {
            dataFakerStrategy {
                isbn13Separator = false
            }
        }.isbn13.isbnHasSeparator().shouldBeFalse()
    }

    @Test
    fun `locale is English by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().countryCode
        }
    }

    @Test
    fun `locale can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        fixture<Person>().countryCode shouldBe "FR"
    }

    @Test
    fun `locale can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        val result = fixture<Person> {
            dataFakerStrategy {
                locale = Locale.GERMAN
            }
        }.countryCode

        result shouldBe "DE"
    }

    @Test
    fun `password length set by default`() {
        val fixture = kotlinFixture {
            dataFakerStrategy()
        }

        repeat(100) {
            val len = fixture<Person>().password.length
            len shouldBeInRange 8..16
        }
    }

    @Test
    fun `password can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                password = Password(minimumLength = 3, maximumLength = 3)
            }
        }

        fixture<Person>().password.length shouldBe 3
    }

    @Test
    fun `password can be overridden in creation`() {
        val fixture = kotlinFixture {
            dataFakerStrategy {
                password = Password(3, 3)
            }
        }

        repeat(100) {
            val password = fixture<Person> {
                dataFakerStrategy {
                    password = Password(5, 10)
                }
            }.password

            password.length shouldBeInRange 5..10
        }
    }

    companion object {
        private fun String.isUUID(): Boolean {
            return try {
                UUID.fromString(this)
                true
            } catch (ignored: Exception) {
                false
            }
        }

        private val amexRegex = "^3[47].*$".toRegex()
        private val jcbRegex = "^(3(?:088|096|112|158|337|5(?:2[89]|[3-8][0-9]))\\d{12})\$".toRegex()
        private val ipV4Regex =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$".toRegex()
        private val ipV6Regex = "^(?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4}\$".toRegex()

        private fun String.isAmex() = matches(amexRegex)
        private fun String.isJcb() = matches(jcbRegex)

        private fun String.isIpV4() = matches(ipV4Regex)
        private fun String.isIpV6() = matches(ipV6Regex)

        private fun String.isbnHasSeparator() = contains("-")
    }
}
