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
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.reflect.KClass

/**
 * Conversions for kotlinx-datetime's `LocalDate` and `LocalDateTime`. `Instant` is [kotlin.time.Instant] in the
 * standard library, which [FakeValueConverter] converts directly.
 */
@SuppressLint("NewApi")
internal object KotlinxDateTimeConverter : OptionalDateTimeConverter {

    override val isAvailable: Boolean by lazy { isClassAvailable("kotlinx.datetime.LocalDate") }

    override fun toZonedDateTime(value: Any, zone: ZoneId): ZonedDateTime? = when (value) {
        is kotlinx.datetime.LocalDate -> value.toJavaLocalDate().atStartOfDay(zone)
        is kotlinx.datetime.LocalDateTime -> value.toJavaLocalDateTime().atZone(zone)
        else -> null
    }

    override fun fromZonedDateTime(dateTime: ZonedDateTime, target: KClass<*>): Any? = when (target) {
        kotlinx.datetime.LocalDate::class -> dateTime.toLocalDate().toKotlinLocalDate()
        kotlinx.datetime.LocalDateTime::class -> dateTime.toLocalDateTime().toKotlinLocalDateTime()
        else -> null
    }
}
