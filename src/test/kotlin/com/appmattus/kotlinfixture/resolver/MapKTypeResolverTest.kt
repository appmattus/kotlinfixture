package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration
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
import kotlin.test.assertTrue

@RunWith(Parameterized::class)
class MapKTypeResolverTest {
    private val resolver =
        CompositeResolver(
            MapKTypeResolver(Configuration()),
            StringResolver(),
            PrimitiveResolver(),
            KTypeResolver(),
            KFunctionResolver(Configuration()),
            ClassResolver(Configuration())
        )

    @Parameterized.Parameter(0)
    lateinit var type: KType

    @Parameterized.Parameter(1)
    lateinit var resultClass: KClass<*>


    @Test
    fun `creates instance`() {
        val result = resolver.resolve(type, resolver)

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
