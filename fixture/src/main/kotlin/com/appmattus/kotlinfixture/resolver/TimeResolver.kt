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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

internal class TimeResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return when (obj) {
            ZonedDateTime::class -> context.generateZonedDateTime()
            LocalDate::class -> context.generateZonedDateTime().toLocalDate()
            LocalTime::class -> context.generateZonedDateTime().toLocalTime()
            LocalDateTime::class -> context.generateZonedDateTime().toLocalDateTime()
            OffsetDateTime::class -> context.generateZonedDateTime().toOffsetDateTime()
            OffsetTime::class -> context.generateZonedDateTime().toOffsetDateTime().toOffsetTime()
            else -> Unresolved
        }
    }

    private fun Context.generateZonedDateTime(): ZonedDateTime =
        (resolve(typeOf<Date>()) as Date).toInstant().atZone(randomZoneId())

    private fun Context.randomZoneId(): ZoneId {
        val zoneIds = ZoneId.getAvailableZoneIds().toList()
        val zoneId = zoneIds[random.nextInt(zoneIds.size)]
        return ZoneId.of(zoneId)
    }
}
