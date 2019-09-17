package com.appmattus.kotlinfixture.config

import java.util.Date

sealed class DateSpecification {
    internal open val min = 0L
    internal open val max = Long.MAX_VALUE

    data class Before(val before: Date) : DateSpecification() {
        override val max = before.time
    }

    data class After(val after: Date) : DateSpecification() {
        override val min = after.time
    }

    data class Between(val start: Date, val end: Date) : DateSpecification() {
        override val min = start.time
        override val max = end.time
    }
}
