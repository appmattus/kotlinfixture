package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.util.UUID

class UuidResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? =
        if (obj == UUID::class) UUID.randomUUID() else Unresolved
}
