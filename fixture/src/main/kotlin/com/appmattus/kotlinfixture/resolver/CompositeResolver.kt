package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved

internal class CompositeResolver(private vararg val resolvers: Resolver) : Resolver, Iterable<Resolver> {

    override fun resolve(context: Context, obj: Any): Any? {
        resolvers.forEach {
            val result = it.resolve(context, obj)

            if (result != Unresolved) {
                return result
            }
        }

        return Unresolved
    }

    override fun iterator() = resolvers.iterator()
}
