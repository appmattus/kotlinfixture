package com.appmattus.kotlinfixture

import kotlin.test.fail

fun assertIsRandom(block: () -> Any?) {
    val initial = block()

    repeat(10) {
        if (initial != block())
            return
    }

    fail()
}
