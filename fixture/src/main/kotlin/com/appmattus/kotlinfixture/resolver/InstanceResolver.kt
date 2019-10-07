package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KType

internal class InstanceResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {

        if (obj is KType) {
            context.configuration.instances[obj]?.let {
                return it()
            }
        }

        return Unresolved
    }
}
