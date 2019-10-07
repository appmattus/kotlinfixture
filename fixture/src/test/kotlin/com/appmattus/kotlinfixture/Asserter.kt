package com.appmattus.kotlinfixture

import kotlin.test.fail

fun assertIsRandom(block: () -> Any?) {
    val initial = block()

    repeat(1000) {
        if (initial != block()) return
    }

    fail("Value always equal to $initial")
}
