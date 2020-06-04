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

import kotlin.test.fail

fun assertIsRandom(block: () -> Any?) {
    val initial = block()

    repeat(1000) {
        if (initial != block()) return
    }

    fail("Value always equal to $initial")
}

fun assertNone(iterations: Int = 1000, block: () -> Boolean) {
    repeat(iterations) {
        if (block()) fail("Value is true")
    }
}
