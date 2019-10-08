package com.appmattus.kotlinfixture

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
    return (this[0].toLong() shl 56
            or (this[1].toLong() and 0xff shl 48)
            or (this[2].toLong() and 0xff shl 40)
            or (this[3].toLong() and 0xff shl 32)
            or (this[4].toLong() and 0xff shl 24)
            or (this[5].toLong() and 0xff shl 16)
            or (this[6].toLong() and 0xff shl 8)
            or (this[7].toLong() and 0xff))
}
