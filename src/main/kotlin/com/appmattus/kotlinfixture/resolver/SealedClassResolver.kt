package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class SealedClassResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        if ((obj as? KClass<*>)?.isSealed == true) {
            obj.sealedSubclasses.shuffled().forEach { subclass ->
                val result = context.resolve(subclass)
                if (result != Unresolved) {
                    return result
                }
            }
        }

        return Unresolved
    }
}
