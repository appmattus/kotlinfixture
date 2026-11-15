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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.decorator.fake.FakeStrategy
import com.appmattus.kotlinfixture.decorator.fake.fakeStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.appmattus.kotlinfixture.typeOf
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame

class FakeValueConverterTest {

    private val zone = ZoneId.systemDefault()
    private val localDate = LocalDate.of(1990, 6, 15)
    private val zonedDateTime = localDate.atStartOfDay(zone)
    private val date = Date.from(zonedDateTime.toInstant())

    @Test
    fun `value matching the property type is returned unchanged`() {
        val value = "string"

        assertSame(value, FakeValueConverter.convert(value, typeOf<String>()))
    }

    @Test
    fun `value matching a nullable property type is returned unchanged`() {
        assertSame(date, FakeValueConverter.convert(date, typeOf<Date?>()))
    }

    @Test
    fun `Date converts to each date and time type in the system default zone`() {
        assertEquals(localDate, FakeValueConverter.convert(date, typeOf<LocalDate>()))
        assertEquals(localDate.atStartOfDay(), FakeValueConverter.convert(date, typeOf<LocalDateTime>()))
        assertEquals(zonedDateTime.toInstant(), FakeValueConverter.convert(date, typeOf<Instant>()))
        assertEquals(zonedDateTime, FakeValueConverter.convert(date, typeOf<ZonedDateTime>()))
        assertEquals(zonedDateTime.toOffsetDateTime(), FakeValueConverter.convert(date, typeOf<OffsetDateTime>()))
        assertEquals(
            date.time,
            (FakeValueConverter.convert(date, typeOf<Calendar>()) as Calendar).timeInMillis
        )
    }

    @Test
    fun `each date and time type converts to Date in the system default zone`() {
        listOf(
            localDate,
            localDate.atStartOfDay(),
            zonedDateTime.toInstant(),
            zonedDateTime,
            zonedDateTime.toOffsetDateTime(),
            GregorianCalendar.from(zonedDateTime)
        ).forEach { value ->
            assertEquals(date, FakeValueConverter.convert(value, typeOf<Date>()), "Converting $value")
        }
    }

    @Test
    fun `Date converts to Kotlin date and time types in the system default zone`() {
        assertEquals(
            localDate.toKotlinLocalDate(),
            FakeValueConverter.convert(date, typeOf<kotlinx.datetime.LocalDate>())
        )
        assertEquals(
            localDate.atStartOfDay().toKotlinLocalDateTime(),
            FakeValueConverter.convert(date, typeOf<kotlinx.datetime.LocalDateTime>())
        )
        assertEquals(
            kotlin.time.Instant.fromEpochMilliseconds(date.time),
            FakeValueConverter.convert(date, typeOf<kotlin.time.Instant>())
        )
    }

    @Test
    fun `Kotlin date and time types convert to Date in the system default zone`() {
        listOf(
            localDate.toKotlinLocalDate(),
            localDate.atStartOfDay().toKotlinLocalDateTime(),
            kotlin.time.Instant.fromEpochMilliseconds(date.time)
        ).forEach { value ->
            assertEquals(date, FakeValueConverter.convert(value, typeOf<Date>()), "Converting $value")
        }
    }

    @Test
    fun `kotlin time Instant keeps nanosecond precision`() {
        val javaInstant = Instant.ofEpochSecond(1_000_000_000L, 123_456_789L)
        val kotlinInstant = kotlin.time.Instant.fromEpochSeconds(1_000_000_000L, 123_456_789L)

        assertEquals(kotlinInstant, FakeValueConverter.convert(javaInstant, typeOf<kotlin.time.Instant>()))
        assertEquals(javaInstant, FakeValueConverter.convert(kotlinInstant, typeOf<Instant>()))
    }

    @Test
    fun `Date converts to java sql types`() {
        val dateTime = zonedDateTime.plusHours(13).plusMinutes(5)
        val value = Date.from(dateTime.toInstant())

        assertEquals(java.sql.Date.valueOf(localDate), FakeValueConverter.convert(value, typeOf<java.sql.Date>()))
        assertEquals(
            java.sql.Timestamp.from(dateTime.toInstant()),
            FakeValueConverter.convert(value, typeOf<java.sql.Timestamp>())
        )
    }

    @Test
    fun `java sql Timestamp keeps nanosecond precision`() {
        val timestamp = java.sql.Timestamp.from(Instant.ofEpochSecond(1_000_000_000L, 123_456_789L))

        assertEquals(timestamp.toInstant(), FakeValueConverter.convert(timestamp, typeOf<Instant>()))
        assertEquals(timestamp, FakeValueConverter.convert(timestamp.toInstant(), typeOf<java.sql.Timestamp>()))
    }

    @Test
    fun `Date converts to Joda-Time types in the system default zone`() {
        assertEquals(
            org.joda.time.LocalDate(1990, 6, 15),
            FakeValueConverter.convert(date, typeOf<org.joda.time.LocalDate>())
        )
        assertEquals(
            org.joda.time.LocalDateTime(1990, 6, 15, 0, 0),
            FakeValueConverter.convert(date, typeOf<org.joda.time.LocalDateTime>())
        )
        assertEquals(
            org.joda.time.Instant(date.time),
            FakeValueConverter.convert(date, typeOf<org.joda.time.Instant>())
        )
        assertEquals(
            date.time,
            (FakeValueConverter.convert(date, typeOf<org.joda.time.DateTime>()) as org.joda.time.DateTime).millis
        )
    }

    @Test
    fun `Joda-Time types convert to Date in the system default zone`() {
        listOf(
            org.joda.time.LocalDate(1990, 6, 15),
            org.joda.time.LocalDateTime(1990, 6, 15, 0, 0),
            org.joda.time.Instant(date.time),
            org.joda.time.DateTime(date.time),
            org.joda.time.MutableDateTime(date.time)
        ).forEach { value ->
            assertEquals(date, FakeValueConverter.convert(value, typeOf<Date>()), "Converting $value")
        }
    }

    @Test
    fun `Date converts to ThreeTenBP types in the system default zone`() {
        val threeTenZone = org.threeten.bp.ZoneId.of(zone.id)

        assertEquals(
            org.threeten.bp.LocalDate.of(1990, 6, 15),
            FakeValueConverter.convert(date, typeOf<org.threeten.bp.LocalDate>())
        )
        assertEquals(
            org.threeten.bp.LocalDateTime.of(1990, 6, 15, 0, 0),
            FakeValueConverter.convert(date, typeOf<org.threeten.bp.LocalDateTime>())
        )
        assertEquals(
            org.threeten.bp.Instant.ofEpochMilli(date.time),
            FakeValueConverter.convert(date, typeOf<org.threeten.bp.Instant>())
        )
        assertEquals(
            org.threeten.bp.LocalDate.of(1990, 6, 15).atStartOfDay(threeTenZone),
            FakeValueConverter.convert(date, typeOf<org.threeten.bp.ZonedDateTime>())
        )
        assertEquals(
            org.threeten.bp.LocalDate.of(1990, 6, 15).atStartOfDay(threeTenZone).toOffsetDateTime(),
            FakeValueConverter.convert(date, typeOf<org.threeten.bp.OffsetDateTime>())
        )
    }

    @Test
    fun `ThreeTenBP types convert to Date in the system default zone`() {
        val threeTenZone = org.threeten.bp.ZoneId.of(zone.id)

        listOf(
            org.threeten.bp.LocalDate.of(1990, 6, 15),
            org.threeten.bp.LocalDateTime.of(1990, 6, 15, 0, 0),
            org.threeten.bp.Instant.ofEpochMilli(date.time),
            org.threeten.bp.LocalDate.of(1990, 6, 15).atStartOfDay(threeTenZone),
            org.threeten.bp.LocalDate.of(1990, 6, 15).atStartOfDay(threeTenZone).toOffsetDateTime()
        ).forEach { value ->
            assertEquals(date, FakeValueConverter.convert(value, typeOf<Date>()), "Converting $value")
        }
    }

    data class LibraryPerson(
        val birthday: org.joda.time.LocalDate,
        val dateOfBirth: org.threeten.bp.LocalDate,
        val dob: java.sql.Date
    )

    @Test
    fun `fixture converts fake values to Joda-Time, ThreeTenBP and java sql property types`() {
        val fixture = kotlinFixture {
            fakeStrategy(
                object : FakeStrategy {
                    override fun fake(context: Context, propertyName: String): Any? = localDate
                }
            )
        }

        val person = fixture<LibraryPerson>()

        assertEquals(org.joda.time.LocalDate(1990, 6, 15), person.birthday)
        assertEquals(org.threeten.bp.LocalDate.of(1990, 6, 15), person.dateOfBirth)
        assertEquals(java.sql.Date.valueOf(localDate), person.dob)
    }

    @Test
    fun `LocalDate converts to GregorianCalendar`() {
        val result = FakeValueConverter.convert(localDate, typeOf<GregorianCalendar>()) as GregorianCalendar

        assertEquals(zonedDateTime, result.toZonedDateTime())
    }

    @Test
    fun `java sql Date converts to LocalDate`() {
        val sqlDate = java.sql.Date(date.time)

        assertEquals(localDate, FakeValueConverter.convert(sqlDate, typeOf<LocalDate>()))
    }

    @Test
    fun `whole numbers convert between types when they fit`() {
        assertEquals(42, FakeValueConverter.convert(42L, typeOf<Int>()))
        assertEquals(42L, FakeValueConverter.convert(42, typeOf<Long>()))
        assertEquals(42.toShort(), FakeValueConverter.convert(42L, typeOf<Short>()))
        assertEquals(42.toByte(), FakeValueConverter.convert(42, typeOf<Byte>()))
    }

    @Test
    fun `whole numbers that do not fit are not converted`() {
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(Long.MAX_VALUE, typeOf<Int>()))
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(Int.MAX_VALUE, typeOf<Short>()))
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(1000, typeOf<Byte>()))
    }

    @Test
    fun `values that cannot be converted are unhandled`() {
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(date, typeOf<String>()))
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert("string", typeOf<LocalDate>()))
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(1.5, typeOf<Int>()))
        assertEquals(Unresolved.Unhandled, FakeValueConverter.convert(42, typeOf<LocalDate>()))
    }

    data class KotlinPerson(
        val birthday: kotlinx.datetime.LocalDate,
        val dateOfBirth: kotlin.time.Instant
    )

    @Test
    fun `fixture converts fake values to Kotlin date and time property types`() {
        val fixture = kotlinFixture {
            fakeStrategy(
                object : FakeStrategy {
                    override fun fake(context: Context, propertyName: String): Any? =
                        if (propertyName == "birthday" || propertyName == "dateOfBirth") date else Unresolved.Unhandled
                }
            )
        }

        val person = fixture<KotlinPerson>()

        assertEquals(localDate.toKotlinLocalDate(), person.birthday)
        assertEquals(kotlin.time.Instant.fromEpochMilliseconds(date.time), person.dateOfBirth)
    }

    data class Person(
        val birthday: LocalDate,
        val dateOfBirth: Date,
        val age: Int,
        val name: Int
    )

    @Test
    fun `fixture converts fake values to the property type`() {
        val strategy = object : FakeStrategy {
            override fun fake(context: Context, propertyName: String): Any? =
                when (propertyName) {
                    "birthday" -> date
                    "dateOfBirth" -> localDate
                    "age" -> 42L
                    "name" -> "not a number"
                    else -> Unresolved.Unhandled
                }
        }

        val fixture = kotlinFixture {
            fakeStrategy(strategy)
        }

        repeat(10) {
            val person = fixture<Person>()

            assertEquals(localDate, person.birthday)
            assertEquals(date, person.dateOfBirth)
            assertEquals(42, person.age)
        }
    }

    @Test
    fun `fixture generates a random value when a fake value cannot be converted`() {
        val fixture = kotlinFixture {
            fakeStrategy(
                object : FakeStrategy {
                    override fun fake(context: Context, propertyName: String): Any? =
                        if (propertyName == "name") "not a number" else Unresolved.Unhandled
                }
            )
        }

        val names = List(100) { fixture<Person>().name }.toSet()

        assertNotEquals(1, names.size)
    }
}
