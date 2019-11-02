package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.nextUuid
import java.io.File

class FileResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj == File::class) {
            return File(context.random.nextUuid().toString())
        } else {
            Unresolved
        }
    }
}
