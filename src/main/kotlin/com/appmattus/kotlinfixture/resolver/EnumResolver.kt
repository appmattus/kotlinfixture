package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.circularIterator
import kotlin.reflect.KClass

class EnumResolver : Resolver {

    private val cache = mutableMapOf<KClass<*>, Iterator<*>>()

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if ((obj as? KClass<*>)?.java?.isEnum == true && obj.java.enumConstants.isNotEmpty()) {
            return cache.computeIfAbsent(obj) {
                obj.java.enumConstants.toList().shuffled().circularIterator()
            }.next()
        }

        return Unresolved
    }
}
