package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.Dictionary
import java.util.Hashtable
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

class HashtableKTypeResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                return null
            }

            val repeatCount = context.configuration.repeatCount()

            val collection = when (obj.classifier as KClass<*>) {

                Dictionary::class,
                Hashtable::class -> Hashtable()

                else -> {
                    @Suppress("USELESS_CAST")
                    null as Dictionary<Any?, Any?>?
                }
            }

            if (collection != null) {
                val keyType = obj.arguments[0].type!!
                val valueType = obj.arguments[1].type!!

                repeat(repeatCount) {
                    collection.put(context.resolve(keyType), context.resolve(valueType))
                }

                return collection
            }
        }

        return Unresolved
    }
}
