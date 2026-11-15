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

import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.IpAddress
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.UserAgent
import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.Test
import java.util.Locale
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinFakerStrategyTest {

    data class Person(
        val firstName: String,
        val creditCard: String,
        val ipAddress: String,
        val countryCode: String,
        val defaultCountry: String,
        val userAgent: String,
        val age: Long,
        val miscellaneous: String
    )

    @Test
    fun `firstName property formatted as UUID when no strategy defined`() {
        val fixture = kotlinFixture()

        assertTrue {
            fixture<Person>().firstName.isUUID()
        }
    }

    @Test
    fun `firstName property formatted by kotlinFakerStrategy is not a UUID`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertFalse {
            fixture<Person>().firstName.isUUID()
        }
    }

    @Test
    fun `firstName property formatted by kotlinFakerStrategy is random`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().firstName
        }
    }

    @Test
    fun `kotlinFakerStrategy generates the same values for the same seed`() {
        val first = kotlinFixture {
            random = kotlin.random.Random(SEED)
            kotlinFakerStrategy()
        }
        val second = kotlinFixture {
            random = kotlin.random.Random(SEED)
            kotlinFakerStrategy()
        }

        assertEquals(first<Person>(), second<Person>())
    }

    @Test
    fun `miscellaneous property not formatted by kotlinFakerStrategy is a UUID`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertTrue {
            fixture<Person>().miscellaneous.isUUID()
        }
    }

    @Test
    fun `age is between 18 and 65`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        repeat(100) {
            assertTrue {
                fixture<Person>().age in 18L..65L
            }
        }
    }

    @Test
    fun `creditCard is any card type by default`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().creditCard.isAmex()
        }
    }

    @Test
    fun `creditCard can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                creditCard = CreditCard.AmericanExpress
            }
        }

        assertTrue {
            fixture<Person>().creditCard.isAmex()
        }
    }

    @Test
    fun `creditCard can be overridden in creation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                creditCard = CreditCard.AmericanExpress
            }
        }

        assertTrue {
            fixture<Person> {
                kotlinFakerStrategy {
                    creditCard = CreditCard.JCB
                }
            }.creditCard.isJcb()
        }
    }

    @Test
    fun `every creditCard type generates a value`() {
        CreditCard.entries.forEach { type ->
            val fixture = kotlinFixture {
                kotlinFakerStrategy {
                    creditCard = type
                }
            }

            repeat(10) {
                val creditCard = fixture<Person>().creditCard
                assertTrue("$type generated an invalid number $creditCard") {
                    creditCard.isValidLuhn()
                }
            }
        }
    }

    @Test
    fun `ipAddress is v4 by default`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertTrue {
            fixture<Person>().ipAddress.isIpV4()
        }
    }

    @Test
    fun `ipAddress can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                ipAddress = IpAddress.V6
            }
        }

        assertTrue {
            fixture<Person>().ipAddress.isIpV6()
        }
    }

    @Test
    fun `ipAddress can be overridden in creation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                ipAddress = IpAddress.V6
            }
        }

        assertTrue {
            fixture<Person> {
                kotlinFakerStrategy {
                    ipAddress = IpAddress.V4
                }
            }.ipAddress.isIpV4()
        }
    }

    @Test
    fun `userAgent is any browser by default`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().userAgent.contains("Firefox")
        }
    }

    @Test
    fun `userAgent can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                userAgent = UserAgent.Firefox
            }
        }

        repeat(100) {
            assertTrue {
                fixture<Person>().userAgent.contains("Firefox")
            }
        }
    }

    @Test
    fun `every userAgent type generates a value`() {
        UserAgent.entries.forEach { type ->
            val fixture = kotlinFixture {
                kotlinFakerStrategy {
                    userAgent = type
                }
            }

            assertTrue("$type generated a blank value") {
                fixture<Person>().userAgent.isNotBlank()
            }
        }
    }

    @Test
    fun `locale is English by default`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy()
        }

        assertEquals("United States of America", fixture<Person>().defaultCountry)
    }

    @Test
    fun `locale can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        assertEquals("France", fixture<Person>().defaultCountry)
    }

    @Test
    fun `locale can be overridden in creation`() {
        val fixture = kotlinFixture {
            kotlinFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        assertEquals(
            "Deutschland",
            fixture<Person> {
                kotlinFakerStrategy {
                    locale = Locale.GERMAN
                }
            }.defaultCountry
        )
    }

    private companion object {
        private const val SEED = 42

        private fun String.isUUID(): Boolean {
            return try {
                UUID.fromString(this)
                true
            } catch (ignored: Exception) {
                false
            }
        }

        private val amexRegex = "^3[47].*$".toRegex()
        private val jcbRegex = "^35(2[8-9]|[3-8][0-9]).*$".toRegex()
        private val ipV4Regex =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$".toRegex()
        private val ipV6Regex = "^(?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4}\$".toRegex()

        private fun String.isValidLuhn(): Boolean {
            if (!matches("^[0-9-]+$".toRegex())) return false

            val digits = filter(Char::isDigit).map(Char::digitToInt)
            return digits.reversed().mapIndexed { index, digit ->
                if (index % 2 == 1) (digit * 2).let { if (it > 9) it - 9 else it } else digit
            }.sum() % 10 == 0
        }

        private fun String.isAmex() = matches(amexRegex)
        private fun String.isJcb() = matches(jcbRegex)

        private fun String.isIpV4() = matches(ipV4Regex)
        private fun String.isIpV6() = matches(ipV6Regex)
    }
}
