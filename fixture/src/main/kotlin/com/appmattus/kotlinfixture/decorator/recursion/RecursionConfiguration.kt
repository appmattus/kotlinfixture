package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

@Suppress("unused")
fun ConfigurationBuilder.recursionStrategy(strategy: RecursionStrategy) {
    strategies[RecursionStrategy::class] = strategy
}
