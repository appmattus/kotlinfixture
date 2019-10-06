package com.appmattus.kotlinfixture

import kotlin.reflect.typeOf

@Suppress("EXPERIMENTAL_API_USAGE_ERROR")
inline fun <reified T> typeOf() = typeOf<T>()
