package com.appmattus.kotlinfixture

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetKTypeKtTest {
    @Test
    fun test() {
        val type = typeOf<List<String>>()

        assertFalse { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    fun test2() {
        val type = typeOf<List<String?>>()

        assertFalse { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    fun test3() {
        val type = typeOf<List<String>?>()

        assertTrue { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
    }

    @Test
    fun test4() {
        val type = typeOf<List<String?>?>()

        assertTrue { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
    }
}
