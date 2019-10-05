package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.IdentityHashMap
import java.util.NavigableMap
import java.util.SortedMap
import java.util.TreeMap
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class MapKTypeResolverTest {

    class Single {
        val context = TestContext(
            Configuration(),
            CompositeResolver(MapKTypeResolver(), StringResolver(), KTypeResolver())
        )

        @Test
        fun `Unknown class returns Unresolved`() {
            val result = context.resolve(Number::class)

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Unknown key type parameter returns Unresolved`() {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = context.resolve(typeOf<Map<Number, String>>())

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Unknown value type parameter returns Unresolved`() {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = context.resolve(typeOf<Map<String, Number>>())

            assertEquals(Unresolved, result)
        }

        @Test
        fun `Length matches configuration value of 3`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 3 }))

            @Suppress("EXPERIMENTAL_API_USAGE_ERROR", "UNCHECKED_CAST")
            val result = context.resolve(typeOf<Map<String, String>>()) as Map<String, String>

            assertEquals(3, result.size)
        }

        @Test
        fun `Length matches configuration value of 7`() {
            val context = context.copy(configuration = Configuration(repeatCount = { 7 }))

            @Suppress("EXPERIMENTAL_API_USAGE_ERROR", "UNCHECKED_CAST")
            val result = context.resolve(typeOf<Map<String, String>>()) as Map<String, String>

            assertEquals(7, result.size)
        }
    }

    @RunWith(Parameterized::class)
    class Parameterised {

        private val context = TestContext(
            Configuration(),
            CompositeResolver(
                MapKTypeResolver(),
                StringResolver(),
                PrimitiveResolver(),
                KTypeResolver(),
                KFunctionResolver(),
                ClassResolver()
            )
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
                arrayOf(typeOf<Map<String, String>>(), Map::class),
                arrayOf(typeOf<SortedMap<String, String>>(), SortedMap::class),
                arrayOf(typeOf<NavigableMap<String, String>>(), NavigableMap::class),
                arrayOf(typeOf<ConcurrentMap<String, String>>(), ConcurrentMap::class),
                arrayOf(typeOf<ConcurrentNavigableMap<String, String>>(), ConcurrentNavigableMap::class),
                arrayOf(typeOf<java.util.AbstractMap<String, String>>(), java.util.AbstractMap::class),
                arrayOf(typeOf<HashMap<String, String>>(), HashMap::class),
                arrayOf(typeOf<LinkedHashMap<String, String>>(), LinkedHashMap::class),
                arrayOf(typeOf<IdentityHashMap<String, String>>(), IdentityHashMap::class),
                arrayOf(typeOf<WeakHashMap<String, String>>(), WeakHashMap::class),
                arrayOf(typeOf<TreeMap<String, String>>(), TreeMap::class),
                arrayOf(typeOf<ConcurrentHashMap<String, String>>(), ConcurrentHashMap::class),
                arrayOf(typeOf<ConcurrentSkipListMap<String, String>>(), ConcurrentSkipListMap::class)
            )
        }
    }
}