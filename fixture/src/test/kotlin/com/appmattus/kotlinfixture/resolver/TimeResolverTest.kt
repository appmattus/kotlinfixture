package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.config.before
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.Date
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class TimeResolverTest {

    class Single {
        private val now = Date()

        private val context = TestContext(
            ConfigurationBuilder().apply { instance<Date> { before(now) } }.build(),
            CompositeResolver(TimeResolver(), InstanceResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Before specification gives date in the past`() {
            repeat(100) {
                val result = context.resolve(ZonedDateTime::class) as ZonedDateTime

                assertTrue {
                    result.toInstant() <= now.toInstant()
                }
            }
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        @Parameterized.Parameter(0)
        lateinit var type: KClass<*>

        @Suppress("UNCHECKED_CAST")
        private val context = TestContext(
            Configuration(),
            CompositeResolver(TimeResolver(), KTypeResolver(), DateResolver())
        )

        @Test
        fun `Class returns date`() {
            val result = context.resolve(type)

            assertNotNull(result)
            assertTrue {
                type.isInstance(result)
            }
        }

        @Test
        fun `Random values returned`() {
            assertIsRandom {
                context.resolve(type)
            }
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{0}")
            fun data() = arrayOf(
                arrayOf(ZonedDateTime::class),
                arrayOf(LocalDate::class),
                arrayOf(LocalTime::class),
                arrayOf(LocalDateTime::class),
                arrayOf(OffsetDateTime::class),
                arrayOf(OffsetTime::class)
            )
        }
    }
}
