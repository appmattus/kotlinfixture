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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.typeOf
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.Month
import org.threeten.bp.MonthDay
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.Period
import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.Date

@Suppress("TooManyFunctions")
internal class ThreeTenResolver : Resolver {

    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        return if (hasThreeTen) {
            when (obj) {
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
                Year::class -> context.generateYear()
                Month::class -> context.generateMonth()
                YearMonth::class -> context.generateYearMonth()
                MonthDay::class -> context.generateMonthDay()
                else -> Unresolved.Unhandled
            }
        } else {
            Unresolved.Unhandled
        }
    }

    private fun Context.generateYear(): Year = Year.of(random.nextInt(Year.MIN_VALUE, Year.MAX_VALUE))

    private fun Context.generateMonth(): Month = Month.entries.toTypedArray().random(random)

    private fun Context.generateYearMonth(): YearMonth = YearMonth.of(generateYear().value, generateMonth())

    private fun Context.generateMonthDay(): MonthDay =
        generateMonth().let { MonthDay.of(it, random.nextInt(1, it.maxLength())) }

    private fun Context.generateInstant(): Instant = DateTimeUtils.toInstant(resolve(typeOf<Date>()) as Date)

    private fun Context.generateZonedDateTime(): ZonedDateTime =
        generateInstant().atZone(resolve(typeOf<ZoneId>()) as ZoneId)

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

    @Suppress("SpellCheckingInspection")
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

    companion object {
        private val hasThreeTen: Boolean by lazy {
            try {
                Class.forName("org.threeten.bp.LocalDate", false, ThreeTenResolver::class.java.classLoader)
                true
            } catch (expected: ClassNotFoundException) {
                false
            }
        }
    }
}
