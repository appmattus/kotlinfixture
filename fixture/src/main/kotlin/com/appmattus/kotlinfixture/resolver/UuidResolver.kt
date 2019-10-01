package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.UUID

class UuidResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == UUID::class) UUID.randomUUID() else Unresolved
}
