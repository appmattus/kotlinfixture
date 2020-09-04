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

import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.IpAddress
import com.appmattus.kotlinfixture.decorator.fake.javafaker.option.Password
import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.Test
import java.util.Locale
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JavaFakerStrategyTest {

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

        assertTrue {
            fixture<Person>().firstName.isUUID()
        }
    }

    @Test
    fun `firstName property formatted by javaFakerStrategy is not a UUID`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertFalse {
            fixture<Person>().firstName.isUUID()
        }
    }

    @Test
    fun `firstName property formatted by javaFakerStrategy is random`() {
        val fixture = kotlinFixture()

        assertIsRandom {
            fixture<Person>().firstName
        }
    }

    @Test
    fun `miscellaneous property not formatted by javaFakerStrategy is a UUID`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertTrue {
            fixture<Person>().miscellaneous.isUUID()
        }
    }

    @Test
    fun `creditCard is any card type by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().creditCard.isAmex()
        }
    }

    @Test
    fun `creditCard can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
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
            javaFakerStrategy {
                creditCard = CreditCard.AmericanExpress
            }
        }

        assertTrue {
            fixture<Person> {
                javaFakerStrategy {
                    creditCard = CreditCard.JCB
                }
            }.creditCard.isJcb()
        }
    }

    @Test
    fun `ipAddress is v4 by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertTrue {
            fixture<Person>().ipAddress.isIpV4()
        }
    }

    @Test
    fun `ipAddress can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
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
            javaFakerStrategy {
                ipAddress = IpAddress.V6
            }
        }

        assertTrue {
            fixture<Person> {
                javaFakerStrategy {
                    ipAddress = IpAddress.V4
                }
            }.ipAddress.isIpV4()
        }
    }

    @Test
    fun `isbn10Separator is false by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertFalse {
            fixture<Person>().isbn10.isbnHasSeparator()
        }
    }

    @Test
    fun `isbn10Separator can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                isbn10Separator = true
            }
        }

        assertTrue {
            fixture<Person>().isbn10.isbnHasSeparator()
        }
    }

    @Test
    fun `isbn10Separator can be overridden in creation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                isbn10Separator = true
            }
        }

        assertFalse {
            fixture<Person> {
                javaFakerStrategy {
                    isbn10Separator = false
                }
            }.isbn10.isbnHasSeparator()
        }
    }

    @Test
    fun `isbn13Separator is false by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertFalse {
            fixture<Person>().isbn13.isbnHasSeparator()
        }
    }

    @Test
    fun `isbn13Separator can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                isbn13Separator = true
            }
        }

        assertTrue {
            fixture<Person>().isbn13.isbnHasSeparator()
        }
    }

    @Test
    fun `isbn13Separator can be overridden in creation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                isbn13Separator = true
            }
        }

        assertFalse {
            fixture<Person> {
                javaFakerStrategy {
                    isbn13Separator = false
                }
            }.isbn13.isbnHasSeparator()
        }
    }

    @Test
    fun `locale is English by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        assertIsRandom {
            fixture<Person>().countryCode
        }
    }

    @Test
    fun `locale can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        assertEquals("FR", fixture<Person>().countryCode)
    }

    @Test
    fun `locale can be overridden in creation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                locale = Locale.FRENCH
            }
        }

        assertEquals(
            "DE",
            fixture<Person> {
                javaFakerStrategy {
                    locale = Locale.GERMAN
                }
            }.countryCode
        )
    }

    @Test
    fun `password length set by default`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        repeat(100) {
            assertTrue {
                val len = fixture<Person>().password.length
                len in 8..16
            }
        }
    }

    @Test
    fun `password can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                password = Password(1, 1)
            }
        }

        assertEquals(1, fixture<Person>().password.length)
    }

    @Test
    fun `password can be overridden in creation`() {
        val fixture = kotlinFixture {
            javaFakerStrategy {
                password = Password(1, 1)
            }
        }

        repeat(100) {
            assertEquals(
                2,
                fixture<Person> {
                    javaFakerStrategy {
                        password = Password(2, 2)
                    }
                }.password.length
            )
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
        private val jcbRegex = "^35(2[8-9]|[3-8][0-9]).*$".toRegex()
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
