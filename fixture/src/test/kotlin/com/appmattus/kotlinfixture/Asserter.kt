package com.appmattus.kotlinfixture

import kotlin.test.fail

fun assertIsRandom(block: () -> Any?) {
    val initial = block()

    repeat(100) {
        if (initial != block())
            return
    }

    fail()
}
