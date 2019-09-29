package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.Date
import kotlin.random.Random

class DateResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj == Date::class) {
            return Date(
                Random.nextLong(
                    context.configuration.dateSpecification.min,
                    context.configuration.dateSpecification.max
                )
            )
        } else {
            Unresolved
        }
    }
}
