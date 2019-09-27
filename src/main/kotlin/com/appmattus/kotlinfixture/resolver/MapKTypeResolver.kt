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
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

class MapKTypeResolver : Resolver {
    override fun resolve(context: Context, obj: Any?): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                return null
            }

            val repeatCount = context.configuration.repeatCount()

            val collection = when (obj.classifier as KClass<*>) {

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

            if (collection != null) {
                val keyType = obj.arguments[0].type
                val valueType = obj.arguments[1].type

                repeat(repeatCount) {
                    collection[context.resolve(keyType)] = context.resolve(valueType)
                }

                return collection
            }
        }

        return Unresolved
    }
}
