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

package com.appmattus.kotlinfixture.decorator.fake

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved

/**
 * Strategy used to provide fake data for named properties, useful if you need to generate objects with pretty data.
 */
interface FakeStrategy {
    /**
     * Return pretty data for a named property, [propertyName], or [Unresolved.Unhandled] if no appropriate value can be
     * generated. [context] should be used to access `random`, or to `resolve` any types through the resolver chain.
     *
     * The value is converted to the property's type where possible: date and time values using the system default
     * time zone, and whole numbers between `Byte`, `Short`, `Int` and `Long` when they fit. Date and time types are
     * `java.util.Date`, `Calendar`, `java.sql.Date`, `java.sql.Timestamp`, the `java.time` types `Instant`,
     * `LocalDate`, `LocalDateTime`, `ZonedDateTime` and `OffsetDateTime`, and `kotlin.time.Instant`, plus, when they are
     * on the classpath, kotlinx-datetime's `LocalDate` and `LocalDateTime`, Joda-Time's `DateTime`, `Instant`,
     * `LocalDate` and `LocalDateTime`, and ThreeTenBP's `Instant`, `LocalDate`, `LocalDateTime`, `ZonedDateTime` and
     * `OffsetDateTime`. Full dates can also be converted to the partial dates `MonthDay`, `YearMonth` and `Year` of
     * these libraries, but not the reverse. A value that cannot be converted is ignored and the property is generated
     * as normal.
     */
    fun fake(context: Context, propertyName: String): Any?
}
