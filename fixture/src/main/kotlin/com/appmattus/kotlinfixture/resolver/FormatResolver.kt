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
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

internal class FormatResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? = when (obj) {
        DateFormat::class,
        SimpleDateFormat::class -> generateSimpleDate(context)

        DecimalFormat::class,
        NumberFormat::class -> generateDecimalFormat(context)

        else -> Unresolved.Unhandled
    }

    private fun generateDecimalFormat(context: Context): NumberFormat? {
        val locale = DecimalFormat.getAvailableLocales().toList().random(context.random)

        @Suppress("MagicNumber")
        return when (context.random.nextInt(4)) {
            3 -> DecimalFormat.getIntegerInstance(locale)
            2 -> DecimalFormat.getPercentInstance(locale)
            1 -> DecimalFormat.getCurrencyInstance(locale)
            else -> DecimalFormat.getNumberInstance(locale)
        }
    }

    private fun generateSimpleDate(context: Context): DateFormat? {
        val style = dateFormatStyles.random(context.random)
        val locale = SimpleDateFormat.getAvailableLocales().toList().random(context.random)

        return SimpleDateFormat.getDateInstance(style, locale)
    }

    companion object {
        private val dateFormatStyles = listOf(DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT)
    }
}
