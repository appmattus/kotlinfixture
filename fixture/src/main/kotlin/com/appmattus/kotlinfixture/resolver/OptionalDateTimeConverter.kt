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

import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.reflect.KClass

/**
 * Converts the date and time types of an optional library, such as Joda-Time, to and from [ZonedDateTime] for
 * [FakeValueConverter]. Implementations must only touch the library's classes when [isAvailable] is true, so the
 * library does not need to be on the classpath.
 */
internal interface OptionalDateTimeConverter {

    /** True when the library is on the classpath. */
    val isAvailable: Boolean

    /** Convert [value] to a [ZonedDateTime] in [zone], or null if [value] is not one of the library's types. */
    fun toZonedDateTime(value: Any, zone: ZoneId): ZonedDateTime?

    /** Convert [dateTime] to [target], or null if [target] is not one of the library's types. */
    fun fromZonedDateTime(dateTime: ZonedDateTime, target: KClass<*>): Any?
}

/** True when the class [className] can be loaded. */
internal fun isClassAvailable(className: String): Boolean = try {
    Class.forName(className, false, OptionalDateTimeConverter::class.java.classLoader)
    true
} catch (expected: ClassNotFoundException) {
    false
}
