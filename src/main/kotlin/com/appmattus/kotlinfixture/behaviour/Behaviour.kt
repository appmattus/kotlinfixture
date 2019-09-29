package com.appmattus.kotlinfixture.behaviour

import com.appmattus.kotlinfixture.resolver.Resolver

interface Behaviour {
    fun transform(resolver: Resolver): Resolver
}
