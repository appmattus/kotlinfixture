package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class SealedClassResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if ((obj as? KClass<*>)?.isSealed == true) {
            obj.sealedSubclasses.shuffled().forEach { subclass ->
                val result = resolver.resolve(subclass, resolver)
                if (result != Unresolved) {
                    return result
                }
            }
        }

        return Unresolved
    }
}
