package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Generator
import kotlin.reflect.KType

internal class InstanceResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {

        if (obj is KType) {
            context.configuration.instances[obj]?.let {
                return with(InstanceGenerator(context)) { it() }
            }
        }

        return Unresolved
    }

    private class InstanceGenerator(context: Context) : Generator<Any?> {
        override val random = context.random
    }
}
