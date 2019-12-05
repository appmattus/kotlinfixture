/*
 * Copyright 2019 Appmattus Limited
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
import com.appmattus.kotlinfixture.typeOf
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.TimeZone

@Suppress("TooManyFunctions")
internal class TimeResolver : Resolver {

    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        return when (obj) {
            Instant::class -> context.generateInstant()
            ZonedDateTime::class -> context.generateZonedDateTime()
            LocalDate::class -> context.generateZonedDateTime().toLocalDate()
            LocalTime::class -> context.generateZonedDateTime().toLocalTime()
            LocalDateTime::class -> context.generateZonedDateTime().toLocalDateTime()
            OffsetDateTime::class -> context.generateZonedDateTime().toOffsetDateTime()
            OffsetTime::class -> context.generateZonedDateTime().toOffsetDateTime().toOffsetTime()
            Period::class -> context.generatePeriod()
            Duration::class -> context.generateDuration()
            ZoneId::class -> context.randomZoneId()
            ZoneOffset::class -> context.generateZoneOffset()
            TimeZone::class -> context.generateTimeZone()
            Year::class -> context.generateYear()
            Month::class -> context.generateMonth()
            YearMonth::class -> context.generateYearMonth()
            MonthDay::class -> context.generateMonthDay()
            else -> Unresolved.Unhandled
        }
    }

    private fun Context.generateYear(): Year = Year.of(random.nextInt(Year.MIN_VALUE, Year.MAX_VALUE))

    private fun Context.generateMonth(): Month = Month.values().random(random)

    private fun Context.generateYearMonth(): YearMonth = YearMonth.of(generateYear().value, generateMonth())

    private fun Context.generateMonthDay(): MonthDay =
        generateMonth().let { MonthDay.of(it, random.nextInt(1, it.maxLength())) }

    private fun Context.generateTimeZone(): TimeZone = TimeZone.getTimeZone(TimeZone.getAvailableIDs().random(random))

    private fun Context.generateInstant(): Instant = (resolve(typeOf<Date>()) as Date).toInstant()

    private fun Context.generateZonedDateTime(): ZonedDateTime = generateInstant().atZone(randomZoneId())

    private fun Context.randomZoneId(): ZoneId = ZoneId.of(ZoneId.getAvailableZoneIds().random(random))

    private fun Context.generateZoneOffset(): ZoneOffset =
        ZoneOffset.ofTotalSeconds(random.nextInt(ZoneOffset.MIN.totalSeconds, ZoneOffset.MAX.totalSeconds))

    @Suppress("MagicNumber")
    private fun Context.generatePeriod(): Period = Period.of(
        random.nextInt(-100, 100),
        random.nextInt(-11, 11),
        random.nextInt(-28, 28)
    )

    @Suppress("MagicNumber")
    private fun Context.generateDuration(): Duration = Duration.of(
        random.nextLong(-1000, 1000),
        generatePreciseChronoUnit()
    )

    private fun Context.generatePreciseChronoUnit() = listOf(
        ChronoUnit.NANOS,
        ChronoUnit.MICROS,
        ChronoUnit.MILLIS,
        ChronoUnit.SECONDS,
        ChronoUnit.MINUTES,
        ChronoUnit.HOURS,
        ChronoUnit.HALF_DAYS,
        ChronoUnit.DAYS
    ).random(random)
}
