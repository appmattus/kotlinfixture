package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver

data class TestContext(override val configuration: Configuration, override val resolver: Resolver) : Context
