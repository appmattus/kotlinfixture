package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.DateSpecification
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DateResolverTest {
    private val now = Date()

    private val resolver = DateResolver(Configuration())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Date class returns date`() {
        val result = resolver.resolve(Date::class, resolver)

        assertNotNull(result)
        assertEquals(Date::class, result::class)
    }

    @Test
    fun `After specification gives date in the future`() {
        val futureResolver = DateResolver(Configuration(DateSpecification.After(now)))

        repeat(100) {
            val result = futureResolver.resolve(Date::class, resolver) as Date

            assertTrue {
                result.time >= now.time
            }
        }
    }

    @Test
    fun `Before specification gives date in the past`() {
        val futureResolver = DateResolver(Configuration(DateSpecification.Before(now)))

        repeat(100) {
            val result = futureResolver.resolve(Date::class, resolver) as Date

            assertTrue {
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Between specification gives date between two dates`() {
        val minTime = now.time - TimeUnit.HOURS.toMillis(1)

        val futureResolver = DateResolver(Configuration(DateSpecification.Between(Date(minTime), now)))

        repeat(100) {
            val result = futureResolver.resolve(Date::class, resolver) as Date

            assertTrue {
                result.time >= minTime
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            resolver.resolve(Date::class, resolver)
        }
    }
}
