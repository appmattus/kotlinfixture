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

package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.strategyOrDefault
import java.util.Stack
import kotlin.reflect.KType

internal class RecursionDecorator : Decorator {

    override fun decorate(resolver: Resolver): Resolver = RecursionResolver(resolver)

    private class RecursionResolver(private val resolver: Resolver) : Resolver {

        private val stack = Stack<KType>()

        @Suppress("ReturnCount")
        override fun resolve(context: Context, obj: Any): Any? {
            val strategy = context.strategyOrDefault<RecursionStrategy>(ThrowingRecursionStrategy)

            if (obj is KType) {
                if (stack.contains(obj)) {
                    return strategy.handleRecursion(obj, stack.toList())
                }

                stack.push(obj)

                try {
                    return resolver.resolve(context, obj)
                } finally {
                    stack.pop()
                }
            }

            return resolver.resolve(context, obj)
        }
    }
}
