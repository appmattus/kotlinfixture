/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.typeOf
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Period
import java.util.Date

internal class JodaTimeResolver : Resolver {

    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        return if (hasJodaTime) {
            when (obj) {
                Instant::class -> context.generateInstant()
                LocalDate::class -> LocalDate(context.generateInstant(), context.generateDateTimeZone())
                LocalTime::class -> LocalTime(context.generateInstant(), context.generateDateTimeZone())
                LocalDateTime::class -> LocalDateTime(context.generateInstant(), context.generateDateTimeZone())
                DateTime::class -> DateTime(context.generateInstant(), context.generateDateTimeZone())
                Period::class -> context.generatePeriod()
                Duration::class -> context.generateDuration()
                DateTimeZone::class -> context.randomDateTimeZone()
                Interval::class -> context.generateInterval()
                else -> Unresolved.Unhandled
            }
        } else {
            Unresolved.Unhandled
        }
    }

    private fun Context.generateInstant(): Instant = Instant(resolve(typeOf<Date>()) as Date)

    private fun Context.generateDateTimeZone(): DateTimeZone = resolve(typeOf<DateTimeZone>()) as DateTimeZone

    private fun Context.randomDateTimeZone(): DateTimeZone =
        DateTimeZone.forID(DateTimeZone.getAvailableIDs().random(random))

    @Suppress("MagicNumber")
    private fun Context.generatePeriod(): Period {
        val randomValue = random.nextInt(-1000, 1000)
        return when (random.nextInt(8)) {
            7 -> Period.days(randomValue)
            6 -> Period.hours(randomValue)
            5 -> Period.millis(randomValue)
            4 -> Period.minutes(randomValue)
            3 -> Period.months(randomValue)
            2 -> Period.seconds(randomValue)
            1 -> Period.weeks(randomValue)
            else -> Period.years(randomValue)
        }
    }

    @Suppress("MagicNumber")
    private fun Context.generateDuration(): Duration {
        val randomValue = random.nextLong(-1000, 1000)
        return when (random.nextInt(4)) {
            3 -> Duration.standardDays(randomValue)
            2 -> Duration.standardHours(randomValue)
            1 -> Duration.standardMinutes(randomValue)
            else -> Duration.standardSeconds(randomValue)
        }
    }

    private fun Context.generateInterval(): Interval {
        val instants = listOf(resolve(typeOf<Instant>()) as Instant, resolve(typeOf<Instant>()) as Instant).sorted()
        return Interval(instants[0], instants[1])
    }

    companion object {
        private val hasJodaTime: Boolean by lazy {
            try {
                Class.forName("org.joda.time.DateTime", false, JodaTimeResolver::class.java.classLoader)
                true
            } catch (expected: ClassNotFoundException) {
                false
            }
        }
    }
}
