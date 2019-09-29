package com.appmattus.kotlinfixture.behaviour.recursion

import kotlin.reflect.KType

internal class NullRecursionHandler : RecursionHandler {

    override fun handleRecursion(type: KType, stack: Collection<KType>) = null
}
