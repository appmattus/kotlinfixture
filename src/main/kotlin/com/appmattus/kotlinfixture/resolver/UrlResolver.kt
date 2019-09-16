package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.net.URL

class UrlResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? =
        if (obj == URL::class) URL("http://localhost") else Unresolved
}
