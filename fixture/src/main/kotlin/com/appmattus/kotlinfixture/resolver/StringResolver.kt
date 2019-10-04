package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.UUID

internal class StringResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == String::class) UUID.randomUUID().toString() else Unresolved
}
