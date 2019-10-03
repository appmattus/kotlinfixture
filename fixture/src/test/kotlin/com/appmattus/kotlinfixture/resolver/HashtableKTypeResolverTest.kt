package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Dictionary
import java.util.Hashtable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class HashtableKTypeResolverTest {

    class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(HashtableKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Unknown key type parameter returns Unresolved`() {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = context.resolve(typeOf<Hashtable<Number, String>>())

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Unknown value type parameter returns Unresolved`() {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = context.resolve(typeOf<Hashtable<String, Number>>())

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Length matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            @Suppress("EXPERIMENTAL_API_USAGE_ERROR", "UNCHECKED_CAST")
            val result = context.resolve(typeOf<Hashtable<String, String>>()) as Hashtable<String, String>

            assertEquals(3, result.size)
        }

        @Test
        fun `Length matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            @Suppress("EXPERIMENTAL_API_USAGE_ERROR", "UNCHECKED_CAST")
            val result = context.resolve(typeOf<Hashtable<String, String>>()) as Hashtable<String, String>

            assertEquals(7, result.size)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        val context = TestContext(
            Configuration(),
            CompositeResolver(HashtableKTypeResolver(), StringResolver(), PrimitiveResolver(), KTypeResolver())
        )

        @Parameterized.Parameter(0)
        lateinit var type: KType

        @Parameterized.Parameter(1)
        lateinit var resultClass: KClass<*>

        @Test
        fun `creates instance`() {
            val result = context.resolve(type)

            assertTrue {
                resultClass.isInstance(result)
            }
        }

        companion object {
            @JvmStatic
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            @Parameterized.Parameters(name = "{1}")
            fun data() = arrayOf(
                arrayOf(typeOf<Dictionary<String, String>>(), Dictionary::class),
                arrayOf(typeOf<Hashtable<String, String>>(), Hashtable::class)
            )
        }
    }
}
