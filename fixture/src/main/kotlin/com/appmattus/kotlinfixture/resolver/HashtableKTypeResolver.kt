package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.Dictionary
import java.util.Hashtable
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class HashtableKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            val collection = createCollection(obj)

            if (collection != null) {
                if (obj.isMarkedNullable && Random.nextBoolean()) {
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

                    collection.put(key, value)
                }

                return collection
            }
        }

        return Unresolved
    }

    private fun createCollection(obj: KType) = when (obj.classifier as KClass<*>) {

        Dictionary::class,
        Hashtable::class -> Hashtable()

        else -> {
            @Suppress("USELESS_CAST")
            null as Dictionary<Any?, Any?>?
        }
    }
}
