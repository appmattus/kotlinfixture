package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved

internal class CharResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == Char::class) (context.random.nextInt(LETTERS) + 'a'.toInt()).toChar() else Unresolved

    companion object {
        private const val LETTERS = 26
    }
}
