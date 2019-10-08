package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.nextUuid

internal class StringResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == String::class) context.random.nextUuid().toString() else Unresolved
}
