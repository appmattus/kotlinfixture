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

import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JavaFakerStrategyTest {

    data class Person(val firstName: String, val miscellaneous: String)

    @Test
    fun `firstName property formatted as UUID when no strategy defined`() {
        val fixture = kotlinFixture()

        repeat(10) {
            assertTrue {
                fixture<Person>().firstName.isUUID()
            }
        }
    }

    @Test
    fun `firstName property formatted by javaFakerStrategy is not a UUID`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        repeat(10) {
            assertFalse {
                fixture<Person>().firstName.isUUID()
            }
        }
    }

    @Test
    fun `miscellaneous property not formatted by javaFakerStrategy is a UUID`() {
        val fixture = kotlinFixture {
            javaFakerStrategy()
        }

        repeat(10) {
            assertTrue {
                fixture<Person>().miscellaneous.isUUID()
            }
        }
    }
}

private fun String.isUUID(): Boolean {
    return try {
        UUID.fromString(this)
        true
    } catch (ignored: Exception) {
        false
    }
}
