package com.appmattus.kotlinfixture.resolver

interface Resolver {
    fun resolve(context: Context, obj: Any?): Any?
}
