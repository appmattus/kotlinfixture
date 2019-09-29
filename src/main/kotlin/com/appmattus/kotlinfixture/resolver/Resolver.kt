package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context

interface Resolver {
    fun resolve(context: Context, obj: Any): Any?
}
