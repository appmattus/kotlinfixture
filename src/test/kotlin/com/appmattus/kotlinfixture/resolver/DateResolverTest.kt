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

    private val context = object : Context {
        override val configuration = Configuration()
        override val rootResolver = DateResolver()
    }

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
    fun `After specification gives date in the future`() {
        val context = object : Context {
            override val configuration = Configuration(dateSpecification = DateSpecification.After(now))
            override val rootResolver = context.rootResolver
        }

        repeat(100) {
            val result = context.resolve(Date::class) as Date

            assertTrue {
                result.time >= now.time
            }
        }
    }

    @Test
    fun `Before specification gives date in the past`() {
        val context = object : Context {
            override val configuration = Configuration(dateSpecification = DateSpecification.Before(now))
            override val rootResolver = context.rootResolver
        }

        repeat(100) {
            val result = context.resolve(Date::class) as Date

            assertTrue {
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Between specification gives date between two dates`() {
        val minTime = now.time - TimeUnit.HOURS.toMillis(1)

        val context = object : Context {
            override val configuration =
                Configuration(dateSpecification = DateSpecification.Between(Date(minTime), now))
            override val rootResolver = context.rootResolver
        }

        repeat(100) {
            val result = context.resolve(Date::class) as Date

            assertTrue {
                result.time >= minTime
                result.time <= now.time
            }
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            context.resolve(Date::class)
        }
    }
}
