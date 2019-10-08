package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class KTypeResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && context.random.nextBoolean()) {
                null
            } else {
                context.resolve(obj.classifier!!)
            }
        } else {
            Unresolved
        }
    }
}
