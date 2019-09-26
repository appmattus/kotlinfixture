package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration

data class TestContext(override val configuration: Configuration, override val resolver: Resolver) : Context
