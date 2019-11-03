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
import java.util.Date
import java.util.concurrent.TimeUnit

internal class DateResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return when (obj) {
            Date::class -> context.generateJavaUtilDate()
            java.sql.Date::class -> java.sql.Date(context.generateJavaUtilDate().time)
            java.sql.Time::class -> java.sql.Time(context.generateJavaUtilDate().time)
            java.sql.Timestamp::class -> java.sql.Timestamp(context.generateJavaUtilDate().time)
            else -> Unresolved
        }
    }

    private fun Context.generateJavaUtilDate(): Date {
        val timeNow = Date().time

        return Date(
            @Suppress("MagicNumber")
            random.nextLong(
                timeNow - TimeUnit.DAYS.toMillis(365),
                timeNow + TimeUnit.DAYS.toMillis(365)
            )
        )
    }
}
