package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class ArrayResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {

        if (obj is KClass<*> && obj.java.isArray && !obj.java.componentType.isPrimitive) {
            val size = context.configuration.repeatCount()

            val array: Any? = java.lang.reflect.Array.newInstance(obj.java.componentType, size)

            for (i in 0 until size) {
                val element = context.resolve(obj.java.componentType.kotlin)
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
