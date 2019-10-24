package com.appmattus.kotlinfixture.decorator.logging

import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun captureSysOut(block: () -> Unit): String {
    ByteArrayOutputStream().use {
        val ps = PrintStream(it)
        val old = System.out

        System.setOut(ps)

        block()

        System.out.flush()
        System.setOut(old)

        return it.toString()
    }
}
