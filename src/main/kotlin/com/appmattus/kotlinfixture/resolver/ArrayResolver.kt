package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.reflect.KClass

class ArrayResolver(private val configuration: Configuration) : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {

        if (obj is KClass<*> && obj.java.isArray && !obj.java.componentType.isPrimitive) {
            val size = configuration.repeatCount()

            val array: Any? = java.lang.reflect.Array.newInstance(obj.java.componentType, size)

            for (i in 0 until size) {
                val element = resolver.resolve(obj.java.componentType.kotlin, resolver)
                if (element == Unresolved) {
                    return Unresolved
                }
                java.lang.reflect.Array.set(array, i, element)
            }

            return array
        }

        return Unresolved
    }
}
