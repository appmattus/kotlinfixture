package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.random.Random

interface Context {
    val configuration: Configuration
    val resolver: Resolver

    val random: Random
        get() = configuration.random

    fun resolve(obj: Any) = resolver.resolve(this, obj)
}
