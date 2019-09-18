package com.appmattus.kotlinfixture.config

import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

data class Configuration(
    val dateSpecification: DateSpecification = DateSpecification.Between(
        Date(Date().time - TimeUnit.DAYS.toMillis(365)),
        Date(Date().time + TimeUnit.DAYS.toMillis(365))
    ),
    val repeatCount: () -> Int = { 5 },
    val properties: Map<KClass<*>, Map<String, Any?>> = emptyMap()
)
