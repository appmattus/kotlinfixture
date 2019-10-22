package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.Date
import java.util.concurrent.TimeUnit

internal class DateResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj == Date::class) {
            val timeNow = Date().time

            return Date(
                @Suppress("MagicNumber")
                context.random.nextLong(
                    timeNow - TimeUnit.DAYS.toMillis(365),
                    timeNow + TimeUnit.DAYS.toMillis(365)
                )
            )
        } else {
            Unresolved
        }
    }
}
