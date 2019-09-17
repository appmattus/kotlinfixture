package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import java.util.Date
import kotlin.random.Random

class DateResolver(private val configuration: Configuration) : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        return if (obj == Date::class) {
            return Date(Random.nextLong(configuration.dateSpecification.min, configuration.dateSpecification.max))
        } else {
            Unresolved
        }
    }
}
