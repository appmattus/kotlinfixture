package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.DateSpecification
import java.util.Calendar
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CalendarResolverTest {
    private val now = Date()

    private val dateResolver = DateResolver(Configuration(DateSpecification.Before(now)))

    private val resolver = CalendarResolver()

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, dateResolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Calendar class returns date`() {
        val result = resolver.resolve(Calendar::class, dateResolver)

        assertNotNull(result)
        assertTrue {
            result is Calendar
        }
    }

    @Test
    fun `Before specification gives date in the past`() {
        repeat(100) {
            val result = resolver.resolve(Calendar::class, dateResolver) as Calendar

            assertTrue {
                result.time.time <= now.time
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(Calendar::class, dateResolver)
        }
    }
}
