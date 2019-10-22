package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.Generator
import com.appmattus.kotlinfixture.config.before
import com.appmattus.kotlinfixture.decorator.logging.LoggingDecorator
import com.appmattus.kotlinfixture.decorator.logging.SysOutLoggingStrategy
import com.appmattus.kotlinfixture.typeOf
import java.util.Calendar
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CalendarResolverTest {
    private val now = Date()

    private val beforeNowGenerator: Generator<Date>.() -> Date = {
        before(now)
    }

    @Suppress("UNCHECKED_CAST")
    private val context = TestContext(
        Configuration(
            instances = mapOf(typeOf<Date>() to beforeNowGenerator as Generator<Any?>.() -> Any?),
            decorators = listOf(LoggingDecorator(SysOutLoggingStrategy()))
        ),
        CompositeResolver(CalendarResolver(), InstanceResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Calendar class returns date`() {
        val result = context.resolve(Calendar::class)

        assertNotNull(result)
        assertTrue {
            result is Calendar
        }
    }

    @Test
    fun `Before specification gives date in the past`() {
        repeat(100) {
            val result = context.resolve(Calendar::class) as Calendar

            assertTrue {
                result.time.time <= now.time
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Calendar::class)
        }
    }
}
