package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.nextUuid
import java.util.UUID

internal class UuidResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == UUID::class) context.random.nextUuid() else Unresolved
}
