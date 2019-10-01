package com.appmattus.kotlinfixture.decorator

import com.appmattus.kotlinfixture.resolver.Resolver

interface Decorator {
    fun decorate(resolver: Resolver): Resolver
}
