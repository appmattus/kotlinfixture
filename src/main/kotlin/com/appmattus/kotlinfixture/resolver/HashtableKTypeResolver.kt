package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import java.util.Dictionary
import java.util.Hashtable
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

class HashtableKTypeResolver(private val configuration: Configuration) : Resolver {
    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                return null
            }

            val repeatCount = configuration.repeatCount()

            val collection = when (obj.classifier as KClass<*>) {

                Dictionary::class,
                Hashtable::class -> Hashtable()

                else -> {
                    @Suppress("USELESS_CAST")
                    null as Dictionary<Any?, Any?>?
                }
            }

            if (collection != null) {
                val keyType = obj.arguments[0].type
                val valueType = obj.arguments[1].type

                repeat(repeatCount) {
                    collection.put(resolver.resolve(keyType, resolver), resolver.resolve(valueType, resolver))
                }

                return collection
            }
        }

        return Unresolved
    }
}
