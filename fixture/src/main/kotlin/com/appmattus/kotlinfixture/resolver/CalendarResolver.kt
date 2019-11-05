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
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

internal class CalendarResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return when (obj) {
            Calendar::class,
            GregorianCalendar::class -> {
                GregorianCalendar().apply {
                    time = context.resolve(typeOf<Date>()) as Date
                }
            }
            else -> Unresolved
        }
    }
}
