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

sealed class Unresolved {
    override fun toString() = "Unresolved"

    object Unhandled : Unresolved()

    data class Unsupported(val message: String) : Unresolved()

    data class CausedBy(val message: String, val causes: List<Unresolved>) : Unresolved()

    data class ByException(val exception: Exception) : Unresolved()
}

fun createUnresolved(message: String, causes: List<Any?> = emptyList()): Unresolved {
    val filtered = causes.filterIsInstance<Unresolved>().filterNot { it is Unresolved.Unhandled }

    return if (filtered.isNotEmpty()) {
        Unresolved.CausedBy(message, filtered)
    } else {
        Unresolved.Unsupported(message)
    }
}
