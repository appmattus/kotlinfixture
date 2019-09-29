package com.appmattus.kotlinfixture.behaviour.recursion

import kotlin.reflect.KType

internal interface RecursionHandler {
    fun handleRecursion(type: KType, stack: Collection<KType>): Any?
}
