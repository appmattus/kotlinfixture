package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.net.URI
import java.net.URL

internal class UriResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == URI::class) (context.resolve(URL::class) as URL).toURI() else Unresolved
}
