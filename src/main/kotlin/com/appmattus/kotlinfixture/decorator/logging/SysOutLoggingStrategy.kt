package com.appmattus.kotlinfixture.decorator.logging

import java.util.Stack

class SysOutLoggingStrategy : LoggingStrategy {
    private val stack = Stack<Any>()

    override fun request(obj: Any) {
        stack.push(obj)

        val indent = "    ".repeat(stack.size - 1)
        println("$indent$obj → ")
    }

    override fun response(obj: Any, result: Result<Any?>) {
        val indent = "    ".repeat(stack.size)
        println("$indent$result")

        stack.pop()
    }
}
