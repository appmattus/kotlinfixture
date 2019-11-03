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
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.Date

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
                else -> Unresolved
            }
        } else {
            Unresolved
        }
    }

    private fun Context.generateInstant(): Instant = DateTimeUtils.toInstant(resolve(typeOf<Date>()) as Date)

    private fun Context.generateZonedDateTime(): ZonedDateTime = generateInstant().atZone(randomZoneId())

    private fun Context.randomZoneId(): ZoneId = ZoneId.of(ZoneId.getAvailableZoneIds().random(random))

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
