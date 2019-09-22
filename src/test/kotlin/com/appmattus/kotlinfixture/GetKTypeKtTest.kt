package com.appmattus.kotlinfixture

import kotlin.reflect.typeOf
import kotlin.test.Test

class GetKTypeKtTest {
    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test() {
        val type = typeOf<List<String>>()

        println(type)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test2() {
        val type = typeOf<List<String?>>()

        println(type)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test3() {
        val type = typeOf<List<String>?>()

        println(type)
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test4() {
        val type = typeOf<List<String?>?>()

        println(type)
    }
}
