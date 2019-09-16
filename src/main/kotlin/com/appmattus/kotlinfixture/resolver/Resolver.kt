package com.appmattus.kotlinfixture.resolver

interface Resolver {
    fun resolve(obj: Any?, resolver: Resolver): Any?
}
