package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.random.Random

class CharResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == Char::class) (Random.nextInt(LETTERS) + 'a'.toInt()).toChar() else Unresolved

    companion object {
        private const val LETTERS = 26
    }
}
