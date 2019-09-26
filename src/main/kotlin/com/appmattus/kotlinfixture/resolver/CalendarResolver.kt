package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class CalendarResolver : Resolver {

    override fun resolve(context: Context, obj: Any?): Any? {
        if (obj == Calendar::class) {
            val date = context.resolve(Date::class) as Date
            return GregorianCalendar().apply {
                time = date
            }
        }

        return Unresolved
    }
}
