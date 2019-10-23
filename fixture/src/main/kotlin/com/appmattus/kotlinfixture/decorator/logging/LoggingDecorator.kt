/*
 * Copyright 2019 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.strategyOrDefault

internal class LoggingDecorator : Decorator {

    override fun decorate(resolver: Resolver): Resolver = LoggingResolver(resolver)

    private class LoggingResolver(
        private val resolver: Resolver
    ) : Resolver {

        override fun resolve(context: Context, obj: Any): Any? {
            val strategy = context.strategyOrDefault<LoggingStrategy>(NoLoggingStrategy)

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

fun ConfigurationBuilder.loggingStrategy(strategy: LoggingStrategy) {
    strategies[LoggingStrategy::class] = strategy
}
