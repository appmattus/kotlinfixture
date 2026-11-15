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
import com.appmattus.kotlinfixture.Unresolved
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Converts a value produced by a fake strategy to the type of the property it is for.
 *
 * Fake strategies only know a property's name, so they can produce a value of a different type to the property, for
 * example a [Date] for a `birthday` property declared as a [LocalDate]. Values that already match are returned
 * unchanged, date and time values are converted between types using the system default time zone, and whole numbers
 * are converted between [Byte], [Short], [Int] and [Long] when they fit. Otherwise [Unresolved.Unhandled] is returned
 * so the property is generated as normal.
 *
 * Date and time types include `java.sql.Date`, `java.sql.Timestamp` and [kotlin.time.Instant], and the date and time
 * types of kotlinx-datetime, Joda-Time and ThreeTenBP when they are on the classpath.
 */
@SuppressLint("NewApi")
internal object FakeValueConverter {

    private val availableConverters: List<OptionalDateTimeConverter> by lazy {
        listOf(KotlinxDateTimeConverter, JodaTimeConverter, ThreeTenConverter).filter { it.isAvailable }
    }

    fun convert(value: Any, type: KType): Any {
        val target = type.classifier as? KClass<*> ?: return value

        return when {
            target.isInstance(value) -> value
            else -> convertDateTime(value, target) ?: convertWholeNumber(value, target) ?: Unresolved.Unhandled
        }
    }

    private fun convertDateTime(value: Any, target: KClass<*>): Any? {
        val dateTime = value.toZonedDateTime() ?: return null

        return when (target) {
            Date::class -> Date.from(dateTime.toInstant())
            java.sql.Date::class -> java.sql.Date.valueOf(dateTime.toLocalDate())
            java.sql.Timestamp::class -> java.sql.Timestamp.from(dateTime.toInstant())
            Calendar::class, GregorianCalendar::class -> GregorianCalendar.from(dateTime)
            Instant::class -> dateTime.toInstant()
            LocalDate::class -> dateTime.toLocalDate()
            LocalDateTime::class -> dateTime.toLocalDateTime()
            ZonedDateTime::class -> dateTime
            OffsetDateTime::class -> dateTime.toOffsetDateTime()
            kotlin.time.Instant::class -> dateTime.toInstant().let {
                kotlin.time.Instant.fromEpochSeconds(it.epochSecond, it.nano)
            }
            else -> availableConverters.firstNotNullOfOrNull { it.fromZonedDateTime(dateTime, target) }
        }
    }

    private fun Any.toZonedDateTime(): ZonedDateTime? {
        val zone = ZoneId.systemDefault()

        return when (this) {
            is java.sql.Timestamp -> toInstant().atZone(zone)
            // Use the epoch milliseconds as java.sql.Date does not support toInstant()
            is Date -> Instant.ofEpochMilli(time).atZone(zone)
            is Calendar -> Instant.ofEpochMilli(timeInMillis).atZone(zone)
            is Instant -> atZone(zone)
            is LocalDate -> atStartOfDay(zone)
            is LocalDateTime -> atZone(zone)
            is ZonedDateTime -> withZoneSameInstant(zone)
            is OffsetDateTime -> atZoneSameInstant(zone)
            is kotlin.time.Instant -> Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong()).atZone(zone)
            else -> availableConverters.firstNotNullOfOrNull { it.toZonedDateTime(this, zone) }
        }
    }

    private fun convertWholeNumber(value: Any, target: KClass<*>): Any? {
        val number = when (value) {
            is Byte, is Short, is Int, is Long -> (value as Number).toLong()
            else -> return null
        }

        return when (target) {
            Byte::class -> number.takeIf { it in Byte.MIN_VALUE..Byte.MAX_VALUE }?.toByte()
            Short::class -> number.takeIf { it in Short.MIN_VALUE..Short.MAX_VALUE }?.toShort()
            Int::class -> number.takeIf { it in Int.MIN_VALUE..Int.MAX_VALUE }?.toInt()
            Long::class -> number
            else -> null
        }
    }
}
