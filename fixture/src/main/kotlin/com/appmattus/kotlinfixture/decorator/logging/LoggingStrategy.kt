package com.appmattus.kotlinfixture.decorator.logging

interface LoggingStrategy {
    fun request(obj: Any)
    fun response(obj: Any, result: Result<Any?>)
}
