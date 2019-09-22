package com.appmattus.kotlinfixture

import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetKTypeKtTest {
    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test() {
        val type = typeOf<List<String>>()

        assertFalse { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test2() {
        val type = typeOf<List<String?>>()

        assertFalse { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test3() {
        val type = typeOf<List<String>?>()

        assertTrue { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
    fun test4() {
        val type = typeOf<List<String?>?>()

        assertTrue { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
    }
}
