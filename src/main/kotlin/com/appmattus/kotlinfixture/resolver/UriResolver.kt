package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import java.net.URI
import java.net.URL

class UriResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? =
        if (obj == URI::class) (resolver.resolve(URL::class, resolver) as URL).toURI() else Unresolved
}
