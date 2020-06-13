/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appmattus.kotlinfixture.decorator.logging

import java.util.Stack
import kotlin.reflect.KType

/**
 * A [LoggingStrategy] that logs to [System.out]
 */
object SysOutLoggingStrategy : LoggingStrategy {
    private val stack = Stack<Any>()

    override fun request(obj: Any) {
        stack.push(obj)

        val indent = "    ".repeat(stack.size - 1)

        val prefix = when (obj) {
            is KType -> @Suppress("SpellCheckingInspection") "ktype "
            else -> ""
        }
        println("$indent$prefix$obj → ")
    }

    override fun response(obj: Any, result: Result<Any?>) {
        val indent = "    ".repeat(stack.size)
        println("$indent$result")

        stack.pop()
    }
}
