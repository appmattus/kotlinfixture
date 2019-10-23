package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.decorator.recursion.RecursionStrategy

@Suppress("unused")
fun ConfigurationBuilder.recursionStrategy(strategy: RecursionStrategy) {
    strategies[RecursionStrategy::class] = strategy
}
