package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

class KTypeResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        return if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                null
            } else {
                resolver.resolve(obj.classifier, resolver)
            }
        } else {
            Unresolved
        }
    }
}
