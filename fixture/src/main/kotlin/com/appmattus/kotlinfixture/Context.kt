package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver

interface Context {
    val configuration: Configuration
    val resolver: Resolver

    fun resolve(obj: Any) = resolver.resolve(this, obj)
}
