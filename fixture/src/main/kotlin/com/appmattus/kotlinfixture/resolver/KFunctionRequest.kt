package com.appmattus.kotlinfixture.resolver

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

data class KFunctionRequest(
    val containingClass: KClass<*>,
    val function: KFunction<*>
)
