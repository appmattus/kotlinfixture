package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
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

internal class MapKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            val collection = createCollection(obj)

            if (collection != null) {
                if (obj.isMarkedNullable && context.random.nextBoolean()) {
                    return null
                }

                val keyType = obj.arguments[0].type!!
                val valueType = obj.arguments[1].type!!

                repeat(context.configuration.repeatCount()) {
                    val key = context.resolve(keyType)
                    val value = context.resolve(valueType)

                    if (key == Unresolved || value == Unresolved) {
                        return Unresolved
                    }

                    collection[key] = value
                }

                return collection
            }
        }

        return Unresolved
    }

    private fun createCollection(obj: KType) = when (obj.classifier as KClass<*>) {

        Map::class,
        java.util.AbstractMap::class,
        HashMap::class -> HashMap()

        SortedMap::class,
        NavigableMap::class,
        TreeMap::class -> TreeMap()

        ConcurrentMap::class,
        ConcurrentHashMap::class -> ConcurrentHashMap()

        ConcurrentNavigableMap::class,
        ConcurrentSkipListMap::class -> ConcurrentSkipListMap()

        LinkedHashMap::class -> LinkedHashMap()
        IdentityHashMap::class -> IdentityHashMap()
        WeakHashMap::class -> WeakHashMap()

        else -> {
            @Suppress("USELESS_CAST")
            null as MutableMap<Any?, Any?>?
        }
    }
}
