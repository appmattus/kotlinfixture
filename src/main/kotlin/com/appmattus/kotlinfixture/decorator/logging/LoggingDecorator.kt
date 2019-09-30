package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver

class LoggingDecorator(private val strategy: LoggingStrategy) : Decorator {

    override fun decorate(resolver: Resolver): Resolver = LoggingResolver(resolver, strategy)

    private class LoggingResolver(
        private val resolver: Resolver,
        private val strategy: LoggingStrategy
    ) : Resolver {

        override fun resolve(context: Context, obj: Any): Any? {
            strategy.request(obj)

            try {
                return resolver.resolve(context, obj).also {
                    strategy.response(obj, Result.success(it))
                }
            } catch (expected: Exception) {
                strategy.response(obj, Result.failure(expected))
                throw expected
            }
        }
    }
}
