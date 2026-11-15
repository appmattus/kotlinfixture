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
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZonedDateTime
import java.time.ZoneId
import kotlin.reflect.KClass

/**
 * Conversions for ThreeTenBP's `Instant`, `LocalDate`, `LocalDateTime`, `ZonedDateTime` and `OffsetDateTime`.
 */
@SuppressLint("NewApi")
internal object ThreeTenConverter : OptionalDateTimeConverter {

    override val isAvailable: Boolean by lazy { isClassAvailable("org.threeten.bp.LocalDate") }

    override fun toZonedDateTime(value: Any, zone: ZoneId): java.time.ZonedDateTime? = when (value) {
        is Instant -> value.toJavaInstant().atZone(zone)
        is LocalDate -> java.time.LocalDate.of(value.year, value.monthValue, value.dayOfMonth).atStartOfDay(zone)
        is LocalDateTime -> java.time.LocalDateTime.of(
            value.year,
            value.monthValue,
            value.dayOfMonth,
            value.hour,
            value.minute,
            value.second,
            value.nano
        ).atZone(zone)
        is ZonedDateTime -> value.toInstant().toJavaInstant().atZone(zone)
        is OffsetDateTime -> value.toInstant().toJavaInstant().atZone(zone)
        else -> null
    }

    override fun fromZonedDateTime(dateTime: java.time.ZonedDateTime, target: KClass<*>): Any? {
        val instant = Instant.ofEpochSecond(dateTime.toEpochSecond(), dateTime.nano.toLong())
        val zone = org.threeten.bp.ZoneId.of(dateTime.zone.id)

        return when (target) {
            Instant::class -> instant
            LocalDate::class -> LocalDate.of(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
            LocalDateTime::class -> LocalDateTime.of(
                dateTime.year,
                dateTime.monthValue,
                dateTime.dayOfMonth,
                dateTime.hour,
                dateTime.minute,
                dateTime.second,
                dateTime.nano
            )
            ZonedDateTime::class -> ZonedDateTime.ofInstant(instant, zone)
            OffsetDateTime::class -> OffsetDateTime.ofInstant(instant, zone)
            else -> null
        }
    }

    private fun Instant.toJavaInstant(): java.time.Instant = java.time.Instant.ofEpochSecond(epochSecond, nano.toLong())
}
