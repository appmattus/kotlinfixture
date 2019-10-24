package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

@Suppress("unused")
fun ConfigurationBuilder.loggingStrategy(strategy: LoggingStrategy) {
    strategies[LoggingStrategy::class] = strategy
}
