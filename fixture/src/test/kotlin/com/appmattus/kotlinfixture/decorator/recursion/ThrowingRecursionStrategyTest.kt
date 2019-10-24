package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.FixtureException
import com.appmattus.kotlinfixture.typeOf
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ThrowingRecursionStrategyTest {

    @Test
    fun `throws illegal state exception when stack is empty`() {
        assertFailsWith<IllegalStateException> {
            ThrowingRecursionStrategy.handleRecursion(typeOf<String>(), emptyList())
        }
    }

    @Test
    fun `throws expected exception when stack is populated`() {
        assertFailsWith<FixtureException> {
            ThrowingRecursionStrategy.handleRecursion(typeOf<String>(), listOf(typeOf<Int>(), typeOf<Float>()))
        }
    }
}
