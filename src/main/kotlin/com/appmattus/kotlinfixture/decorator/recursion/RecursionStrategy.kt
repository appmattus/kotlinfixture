package com.appmattus.kotlinfixture.decorator.recursion

import kotlin.reflect.KType

interface RecursionStrategy {
    fun handleRecursion(type: KType, stack: Collection<KType>): Any?
}
