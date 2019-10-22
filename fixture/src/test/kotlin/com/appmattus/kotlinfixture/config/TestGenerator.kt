package com.appmattus.kotlinfixture.config

import kotlin.random.Random

object TestGenerator : Generator<Any?> {
    override val random = Random
}
