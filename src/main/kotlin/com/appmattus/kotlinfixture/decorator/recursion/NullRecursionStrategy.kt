package com.appmattus.kotlinfixture.decorator.recursion

import kotlin.reflect.KType

class NullRecursionStrategy : RecursionStrategy {

    override fun handleRecursion(type: KType, stack: Collection<KType>) = null
}
