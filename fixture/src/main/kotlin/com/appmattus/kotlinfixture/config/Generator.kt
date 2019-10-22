package com.appmattus.kotlinfixture.config

import kotlin.random.Random

interface Generator<T> {
    val random: Random
}
