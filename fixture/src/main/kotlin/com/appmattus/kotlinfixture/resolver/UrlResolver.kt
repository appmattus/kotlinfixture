package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.net.URL

internal class UrlResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == URL::class) URL("http://localhost") else Unresolved
}
