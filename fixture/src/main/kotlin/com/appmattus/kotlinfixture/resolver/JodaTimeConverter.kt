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

import android.annotation.SuppressLint
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.MonthDay
import org.joda.time.ReadableInstant
import org.joda.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Conversions for Joda-Time's `DateTime`, `Instant`, `LocalDate` and `LocalDateTime`, and to `MonthDay` and
 * `YearMonth`. Any `ReadableInstant`, such as `MutableDateTime`, can also be converted from.
 */
@SuppressLint("NewApi")
internal object JodaTimeConverter : OptionalDateTimeConverter {

    override val isAvailable: Boolean by lazy { isClassAvailable("org.joda.time.DateTime") }

    override fun toZonedDateTime(value: Any, zone: ZoneId): ZonedDateTime? = when (value) {
        is ReadableInstant -> java.time.Instant.ofEpochMilli(value.millis).atZone(zone)
        is LocalDate -> java.time.LocalDate.of(value.year, value.monthOfYear, value.dayOfMonth).atStartOfDay(zone)
        is LocalDateTime -> java.time.LocalDateTime.of(
            value.year,
            value.monthOfYear,
            value.dayOfMonth,
            value.hourOfDay,
            value.minuteOfHour,
            value.secondOfMinute,
            TimeUnit.MILLISECONDS.toNanos(value.millisOfSecond.toLong()).toInt()
        ).atZone(zone)
        else -> null
    }

    override fun fromZonedDateTime(dateTime: ZonedDateTime, target: KClass<*>): Any? = when (target) {
        DateTime::class -> DateTime(
            dateTime.toInstant().toEpochMilli(),
            DateTimeZone.forTimeZone(TimeZone.getTimeZone(dateTime.zone))
        )
        Instant::class -> Instant(dateTime.toInstant().toEpochMilli())
        LocalDate::class -> LocalDate(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
        MonthDay::class -> MonthDay(dateTime.monthValue, dateTime.dayOfMonth)
        YearMonth::class -> YearMonth(dateTime.year, dateTime.monthValue)
        LocalDateTime::class -> LocalDateTime(
            dateTime.year,
            dateTime.monthValue,
            dateTime.dayOfMonth,
            dateTime.hour,
            dateTime.minute,
            dateTime.second,
            TimeUnit.NANOSECONDS.toMillis(dateTime.nano.toLong()).toInt()
        )
        else -> null
    }
}
