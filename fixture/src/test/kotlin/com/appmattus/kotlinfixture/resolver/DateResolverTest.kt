package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DateResolverTest {
    private val now = Date()

    private val context = TestContext(Configuration(), DateResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Date class returns date`() {
        val result = context.resolve(Date::class)

        assertNotNull(result)
        assertEquals(Date::class, result::class)
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Date::class)
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = context.seedRandom().resolve(Date::class) as Date
        val value2 = context.seedRandom().resolve(Date::class) as Date

        assertEquals(value1, value2)
    }
}
