package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

class ObjectResolver : Resolver {

    override fun resolve(context: Context, obj: Any?): Any? = (obj as? KClass<*>)?.objectInstance ?: Unresolved
}
