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

package io.github.detomarco.kotlinfixture

import io.github.detomarco.kotlinfixture.resolver.Resolver
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

/**
 * Used by [Resolver] when it is unable to resolve an object.
 */
sealed class Unresolved {
    /**
     * Use [Unhandled] when a resolver does not handle a particular type
     */
    object Unhandled : Unresolved()

    /**
     * Use [NotSupported] when a resolver handles a particular type but is unable to
     */
    class NotSupported(val message: String, val causes: List<Unresolved> = emptyList()) : Unresolved()

    /**
     * Use [WithException] when a resolver handles a particular type but is unable to due to a caught exception
     */
    class WithException(val message: String, val exception: Exception) : Unresolved()

    override fun toString(): String {
        val writer = StringWriter()

        stackTrace(writer, 0)
        writer.flush()

        return writer.toString()
    }

    private fun stackTrace(writer: Writer, depth: Int = 0) {
        when (this) {
            is NotSupported -> {
                writer.write("${"    ".repeat(depth)}$message\n")
                causes.forEach { it.stackTrace(writer, depth + 1) }
            }
            is WithException -> {
                writer.write("${"    ".repeat(depth)}$message\n")

                val stringWriter = StringWriter()

                val stackTrace = PrintWriter(stringWriter).use {
                    exception.printStackTrace(it)
                    it.flush()
                    stringWriter.toString()
                }

                writer.write(stackTrace.prependIndent("    ".repeat(depth + 1)))
                writer.write("\n")
            }
            Unhandled -> Unit
        }
    }

    companion object {
        /**
         * Create an [Unresolved.NotSupported] from many [causes] which are [Unresolved].
         */
        fun createUnresolved(message: String, causes: List<Any?> = emptyList()): Unresolved {
            val filtered = causes.filterIsInstance<Unresolved>().filterNot { it is Unresolved.Unhandled }

            return if (filtered.size == 1) {
                // Unwrap single causes to reduce nesting
                filtered.first()
            } else {
                NotSupported(message, filtered)
            }
        }
    }
}
