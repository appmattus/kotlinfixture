package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.CompositeResolver
import com.appmattus.kotlinfixture.resolver.Resolver

internal data class ContextImpl(override val configuration: Configuration) : Context {
    private val baseResolver = CompositeResolver(configuration.resolvers) as Resolver

    override val resolver = configuration.decorators.fold(baseResolver) { resolver, decorator ->
        decorator.decorate(resolver)
    }
}
