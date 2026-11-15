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

import com.appmattus.kotlinfixture.kotlinFixture
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertTrue

class DataFakerPropertyTypeTest {

    data class Person(
        val birthday: LocalDate,
        val dateOfBirth: Date,
        val dob: LocalDate?,
        val age: Int
    )

    data class PersonWithLongAge(val age: Long)

    data class KotlinPerson(
        val birthday: kotlinx.datetime.LocalDate,
        val dateOfBirth: kotlinx.datetime.LocalDateTime
    )

    private val fixture = kotlinFixture {
        dataFakerStrategy()
    }

    @Test
    fun `birthday can be a LocalDate or a Date`() {
        repeat(100) {
            val person = fixture<Person>()

            assertTrue("${person.birthday} is not an adult's birthday") { person.birthday.isAdultBirthday() }
            assertTrue("${person.dateOfBirth} is not an adult's birthday") {
                person.dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAdultBirthday()
            }
        }
    }

    @Test
    fun `birthday can be a kotlinx-datetime type`() {
        repeat(100) {
            val person = fixture<KotlinPerson>()

            assertTrue("${person.birthday} is not an adult's birthday") {
                person.birthday.toJavaLocalDate().isAdultBirthday()
            }
            assertTrue("${person.dateOfBirth} is not an adult's birthday") {
                person.dateOfBirth.toJavaLocalDateTime().toLocalDate().isAdultBirthday()
            }
        }
    }

    @Test
    fun `age can be an Int or a Long`() {
        repeat(100) {
            assertTrue { fixture<Person>().age in 18..65 }
            assertTrue { fixture<PersonWithLongAge>().age in 18L..65L }
        }
    }

    private fun LocalDate.isAdultBirthday() =
        isBefore(LocalDate.now().minusYears(17)) && isAfter(LocalDate.now().minusYears(67))
}
