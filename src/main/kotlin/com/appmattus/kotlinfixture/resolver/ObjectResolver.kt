package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class ObjectResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? =
        (obj as? KClass<*>)?.objectInstance ?: Unresolved
}
