package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.typeOf
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class NullRecursionStrategyTest {
    @Test
    fun `throws illegal state exception when stack is empty`() {
        assertFailsWith<IllegalStateException> {
            NullRecursionStrategy.handleRecursion(typeOf<String>(), emptyList())
        }
    }

    @Test
    fun `returns null when stack is populated`() {
        assertNull(NullRecursionStrategy.handleRecursion(typeOf<String>(), listOf(typeOf<Int>(), typeOf<Float>())))
    }
}
