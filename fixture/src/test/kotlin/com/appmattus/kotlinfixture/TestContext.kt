package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.random.Random

data class TestContext(override val configuration: Configuration, override val resolver: Resolver) : Context {
    fun seedRandom() = this.copy(configuration = configuration.copy(random = Random(0)))
}
