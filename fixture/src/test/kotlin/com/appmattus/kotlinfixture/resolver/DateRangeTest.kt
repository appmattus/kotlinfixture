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

import com.appmattus.kotlinfixture.config.before
import com.appmattus.kotlinfixture.kotlinFixture
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Generated dates fall within ten years of 2020, and partial dates (years, year-months and month-days) are realistic,
 * follow the configured date range, and can be any valid day of the month.
 */
class DateRangeTest {

    private val fixture = kotlinFixture()

    @Test
    fun `dates are within ten years of 2020`() {
        repeat(SAMPLES) {
            val year = fixture<Date>().toInstant().atZone(ZoneId.systemDefault()).year

            assertTrue("$year is outside 2010..2030") { year in 2009..2030 }
        }
    }

    @Test
    fun `java time LocalDate is within ten years of 2020`() {
        repeat(SAMPLES) {
            assertTrue { fixture<LocalDate>().year in 2009..2030 }
        }
    }

    @Test
    fun `year based types are realistic`() {
        repeat(SAMPLES) {
            assertTrue { fixture<java.time.Year>().value in 2009..2030 }
            assertTrue { fixture<java.time.YearMonth>().year in 2009..2030 }
            assertTrue { fixture<org.threeten.bp.Year>().value in 2009..2030 }
            assertTrue { fixture<org.threeten.bp.YearMonth>().year in 2009..2030 }
            assertTrue { fixture<org.joda.time.YearMonth>().year in 2009..2030 }
        }
    }

    @Test
    fun `year based types follow the configured date range`() {
        val cutOff = Date.from(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val fixture = kotlinFixture {
            factory<Date> { before(cutOff) }
        }

        repeat(SAMPLES) {
            assertTrue { fixture<java.time.Year>().value <= 2015 }
            assertTrue { fixture<java.time.YearMonth>().year <= 2015 }
            assertTrue { fixture<org.threeten.bp.YearMonth>().year <= 2015 }
            assertTrue { fixture<org.joda.time.YearMonth>().year <= 2015 }
        }
    }

    @Test
    fun `java time MonthDay can be the last day of the month`() {
        val values = List(MONTH_DAY_SAMPLES) { fixture<java.time.MonthDay>() }

        assertTrue { values.any { it.month == Month.JANUARY && it.dayOfMonth == 31 } }
        assertTrue { values.any { it.month == Month.FEBRUARY && it.dayOfMonth == 29 } }
    }

    @Test
    fun `ThreeTenBP MonthDay can be the last day of the month`() {
        val values = List(MONTH_DAY_SAMPLES) { fixture<org.threeten.bp.MonthDay>() }

        assertTrue { values.any { it.month == org.threeten.bp.Month.JANUARY && it.dayOfMonth == 31 } }
        assertTrue { values.any { it.month == org.threeten.bp.Month.FEBRUARY && it.dayOfMonth == 29 } }
    }

    @Test
    fun `Joda-Time YearMonth and MonthDay are valid`() {
        repeat(SAMPLES) {
            val yearMonth = fixture<org.joda.time.YearMonth>()
            assertTrue("$yearMonth") { yearMonth.monthOfYear in 1..12 }

            val monthDay = fixture<org.joda.time.MonthDay>()
            assertTrue("$monthDay") { monthDay.monthOfYear in 1..12 }
            assertTrue("$monthDay") { monthDay.dayOfMonth in 1..monthDay.dayOfMonth().maximumValue }
        }
    }

    @Test
    fun `Joda-Time MonthDay can be the last day of the month`() {
        val values = List(MONTH_DAY_SAMPLES) { fixture<org.joda.time.MonthDay>() }

        assertTrue { values.any { it.monthOfYear == 1 && it.dayOfMonth == 31 } }
        assertTrue { values.any { it.monthOfYear == 2 && it.dayOfMonth == 29 } }
    }

    @Test
    fun `Joda-Time YearMonth and MonthDay are random`() {
        assertEquals(true, List(SAMPLES) { fixture<org.joda.time.YearMonth>() }.toSet().size > 1)
        assertEquals(true, List(SAMPLES) { fixture<org.joda.time.MonthDay>() }.toSet().size > 1)
    }

    private companion object {
        private const val SAMPLES = 500
        private const val MONTH_DAY_SAMPLES = 5000
    }
}
