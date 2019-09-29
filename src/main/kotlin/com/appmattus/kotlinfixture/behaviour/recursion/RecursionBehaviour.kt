package com.appmattus.kotlinfixture.behaviour.recursion

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.behaviour.Behaviour
import com.appmattus.kotlinfixture.resolver.Resolver
import java.util.Stack
import kotlin.reflect.KType

internal class RecursionBehaviour(
    private val recursionHandler: RecursionHandler
) : Behaviour {

    override fun transform(resolver: Resolver): Resolver = RecursionResolver(resolver, recursionHandler)

    private class RecursionResolver(
        private val resolver: Resolver,
        private val recursionHandler: RecursionHandler
    ) : Resolver {

        private val stack = Stack<KType>()

        override fun resolve(context: Context, obj: Any): Any? {
            if (obj is KType) {
                if (stack.contains(obj)) {
                    return recursionHandler.handleRecursion(obj, stack)
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
