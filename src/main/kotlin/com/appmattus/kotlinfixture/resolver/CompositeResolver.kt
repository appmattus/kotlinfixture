package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved

class CompositeResolver(private vararg val resolvers: Resolver) : Resolver, Iterable<Resolver> {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        println("Resolving: $obj")

        resolvers.forEach {
            val result = it.resolve(obj, resolver)

            if (result != Unresolved) {
                return result
            }
        }

        return Unresolved
    }

    override fun iterator() = resolvers.iterator()
}
