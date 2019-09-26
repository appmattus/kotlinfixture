package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration

interface Context {
    val configuration: Configuration
    val rootResolver: Resolver

    fun resolve(obj: Any?) = rootResolver.resolve(this, obj)
}
