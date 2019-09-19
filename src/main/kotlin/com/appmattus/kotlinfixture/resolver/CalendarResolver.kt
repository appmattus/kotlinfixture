package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class CalendarResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj == Calendar::class) {
            val date = resolver.resolve(Date::class, resolver) as Date
            return GregorianCalendar().apply {
                time = date
            }
        }

        return Unresolved
    }
}
