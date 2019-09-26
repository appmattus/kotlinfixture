package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration

interface Context {
    val configuration: Configuration
    val resolver: Resolver

    fun resolve(obj: Any?) = resolver.resolve(this, obj)
}
