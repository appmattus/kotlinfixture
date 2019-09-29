package com.appmattus.kotlinfixture.behaviour.recursion

import com.appmattus.kotlinfixture.FixtureException
import kotlin.reflect.KType

internal class ThrowingRecursionHandler : RecursionHandler {

    override fun handleRecursion(type: KType, stack: Collection<KType>): Any? {
        val errorMessage = "Unable to create ${stack.first()} with circular reference: ${stack.toStackString(type)}"
        throw FixtureException(errorMessage)
    }

    private fun Collection<KType>.toStackString(type: KType) = (this + type).joinToString(separator = " → ")
}
