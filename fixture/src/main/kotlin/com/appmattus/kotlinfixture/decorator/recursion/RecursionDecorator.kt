package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.Resolver
import java.util.Stack
import kotlin.reflect.KType

class RecursionDecorator(
    private val strategy: RecursionStrategy
) : Decorator {

    override fun decorate(resolver: Resolver): Resolver = RecursionResolver(resolver, strategy)

    private class RecursionResolver(
        private val resolver: Resolver,
        private val recursionStrategy: RecursionStrategy
    ) : Resolver {

        private val stack = Stack<KType>()

        @Suppress("ReturnCount")
        override fun resolve(context: Context, obj: Any): Any? {
            if (obj is KType) {
                if (stack.contains(obj)) {
                    return recursionStrategy.handleRecursion(obj, stack)
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
