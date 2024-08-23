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

package io.github.detomarco.kotlinfixture

import java.util.UUID
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.random.Random

@Suppress("MagicNumber")
internal fun Random.nextUuid(): UUID {
    val mostSigBits = ByteArray(8).let(::nextBytes).apply {
        this[6] = 0x40.toByte() or (this[6] and 0x0f.toByte())
    }.toLong()

    val leastSigBits = ByteArray(8).let(::nextBytes).apply {
        this[0] = 0x80.toByte() or (this[0] and 0x3f.toByte())
    }.toLong()

    return UUID(mostSigBits, leastSigBits)
}

@Suppress("MagicNumber")
private fun ByteArray.toLong(): Long {
    return (
        this[0].toLong() shl 56
            or (this[1].toLong() and 0xff shl 48)
            or (this[2].toLong() and 0xff shl 40)
            or (this[3].toLong() and 0xff shl 32)
            or (this[4].toLong() and 0xff shl 24)
            or (this[5].toLong() and 0xff shl 16)
            or (this[6].toLong() and 0xff shl 8)
            or (this[7].toLong() and 0xff)
        )
}
