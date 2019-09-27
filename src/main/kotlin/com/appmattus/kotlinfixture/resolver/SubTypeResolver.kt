package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class SubTypeResolver : Resolver {

    override fun resolve(context: Context, obj: Any?): Any? {

        if (obj is KClass<*>) {
            context.configuration.subTypes[obj]?.let {
                println(it)
                return context.resolve(it)
            }
        }

        return Unresolved
    }
}
