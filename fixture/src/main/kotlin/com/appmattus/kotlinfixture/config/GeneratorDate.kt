package com.appmattus.kotlinfixture.config

import java.util.Date

fun Generator<Date>.before(before: Date) = Date(random.nextLong(0L, before.time))

fun Generator<Date>.after(after: Date) = Date(random.nextLong(after.time, Long.MAX_VALUE))

fun Generator<Date>.between(start: Date, end: Date) = Date(random.nextLong(start.time, end.time))
