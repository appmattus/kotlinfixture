package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context

class TestResolver : Resolver {
    override fun resolve(context: Context, obj: Any?) = obj
}
