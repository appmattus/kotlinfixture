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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.typeOf
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
import kotlin.reflect.KClass

@Suppress("TooManyFunctions")
internal class ThreeTenResolver : Resolver {

    @Suppress("ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        return if (hasThreeTen) {
            when ((obj as? KClass<*>)?.javaObjectType) {
                Instant::class.java -> context.generateInstant()
                ZonedDateTime::class.java -> context.generateZonedDateTime()
                LocalDate::class.java -> context.generateZonedDateTime().toLocalDate()
                LocalTime::class.java -> context.generateZonedDateTime().toLocalTime()
                LocalDateTime::class.java -> context.generateZonedDateTime().toLocalDateTime()
                OffsetDateTime::class.java -> context.generateZonedDateTime().toOffsetDateTime()
                OffsetTime::class.java -> context.generateZonedDateTime().toOffsetDateTime().toOffsetTime()
                Period::class.java -> context.generatePeriod()
                Duration::class.java -> context.generateDuration()
                ZoneId::class.java -> context.randomZoneId()
                ZoneOffset::class.java -> context.generateZoneOffset()
                Year::class.java -> context.generateYear()
                Month::class.java -> context.generateMonth()
                YearMonth::class.java -> context.generateYearMonth()
                MonthDay::class.java -> context.generateMonthDay()
                else -> Unresolved.Unhandled
            }
        } else {
            Unresolved.Unhandled
        }
    }

    // Derived from a generated date so the year is realistic and follows the configured date range
    private fun Context.generateYear(): Year = Year.of(generateZonedDateTime().year)

    private fun Context.generateMonth(): Month = Month.values().random(random)

    private fun Context.generateYearMonth(): YearMonth = YearMonth.from(generateZonedDateTime())

    private fun Context.generateMonthDay(): MonthDay =
        generateMonth().let { MonthDay.of(it, random.nextInt(1, it.maxLength() + 1)) }

    private fun Context.generateInstant(): Instant = DateTimeUtils.toInstant(resolve(typeOf<Date>()) as Date)

    private fun Context.generateZonedDateTime(): ZonedDateTime =
        generateInstant().atZone(resolve(typeOf<ZoneId>()) as ZoneId)

    private fun Context.randomZoneId(): ZoneId = ZoneId.of(zoneIds.random(random))

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
        // Listed once, as getting the available zone IDs copies them and picking from a set walks it. Sorted so
        // a seeded fixture picks the same zone whatever order the library returns them in.
        private val zoneIds: List<String> by lazy { ZoneId.getAvailableZoneIds().sorted() }

        private val hasThreeTen: Boolean by lazy {
            try {
                @Suppress("SpellCheckingInspection")
                Class.forName("org.threeten.bp.LocalDate", false, ThreeTenResolver::class.java.classLoader)
                true
            } catch (expected: ClassNotFoundException) {
                false
            }
        }
    }
}
