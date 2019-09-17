package com.appmattus.kotlinfixture.config

import java.util.Date
import java.util.concurrent.TimeUnit

data class Configuration(
    val dateSpecification: DateSpecification = DateSpecification.Between(
        Date(Date().time - TimeUnit.DAYS.toMillis(365)),
        Date(Date().time + TimeUnit.DAYS.toMillis(365))
    ),
    val repeatCount: () -> Int = { 5 }
)
