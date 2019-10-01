package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.FixtureException
import kotlin.reflect.KType

class ThrowingRecursionStrategy : RecursionStrategy {

    override fun handleRecursion(type: KType, stack: Collection<KType>): Any? {
        val errorMessage = "Unable to create ${stack.first()} with circular reference: ${stack.toStackString(type)}"
        throw FixtureException(errorMessage)
    }

    private fun Collection<KType>.toStackString(type: KType) = (this + type).joinToString(separator = " → ")
}
