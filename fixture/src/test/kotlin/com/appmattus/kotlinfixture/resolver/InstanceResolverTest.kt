package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.typeOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InstanceResolverTest {

    @Test
    fun `Unresolved returned when no mapping found`() {
        val context = TestContext(Configuration(), InstanceResolver())

        assertEquals(Unresolved, context.resolve(typeOf<Number>()))
    }

    @Test
    fun `Instance returned when mapping found`() {
        val configuration = ConfigurationBuilder().apply {
            instance<Number> { 12 }
        }.build()
        val context = TestContext(configuration, InstanceResolver())

        assertEquals(12, context.resolve(typeOf<Number>()))
    }

    @Test
    fun `Instance mapping can return random value`() {
        val configuration = ConfigurationBuilder().apply {
            instance<Number> { Random.nextInt(1, 5) }
        }.build()
        val context = TestContext(configuration, InstanceResolver())

        repeat(100) {
            assertTrue(context.resolve(typeOf<Number>()) in 1..5)
        }
    }
}
