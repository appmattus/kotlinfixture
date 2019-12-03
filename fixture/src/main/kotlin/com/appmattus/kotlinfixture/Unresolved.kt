/*
 * Copyright 2019 Appmattus Limited
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

package com.appmattus.kotlinfixture

import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

sealed class Unresolved {
    override fun toString() = "Unresolved"

    object Unhandled : Unresolved()

    data class Unsupported(val message: String) : Unresolved()

    data class CausedBy(val message: String, val causes: List<Unresolved>) : Unresolved()

    data class ByException(val exception: Exception) : Unresolved()

    fun stackTrace(): String {

        val writer = StringWriter()

        stackTrace(writer, 0)
        writer.flush()

        return writer.toString()
    }

    private fun stackTrace(writer: Writer, depth: Int = 0) {
        when (this) {
            is CausedBy -> {
                writer.write("${"    ".repeat(depth)}$message\n")
                causes.forEach { it.stackTrace(writer, depth + 1) }
            }
            is ByException -> {
                val stringWriter = StringWriter()

                val stackTrace = PrintWriter(stringWriter).use {
                    exception.printStackTrace(it)
                    it.flush()
                    stringWriter.toString()
                }

                writer.write(stackTrace.prependIndent("    ".repeat(depth)))
                writer.write("\n")
            }
            is Unsupported -> writer.write("${"    ".repeat(depth)}$message\n")
        }
    }
}

fun createUnresolved(message: String, causes: List<Any?> = emptyList()): Unresolved {
    val filtered = causes.filterIsInstance<Unresolved>().filterNot { it is Unresolved.Unhandled }

    return if (filtered.isNotEmpty()) {
        if (filtered.size > 1) {
            Unresolved.CausedBy(message, filtered)
        } else {
            filtered.first()
        }
    } else {
        Unresolved.Unsupported(message)
    }
}
