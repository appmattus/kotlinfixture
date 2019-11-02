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

package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.FixtureException
import com.appmattus.kotlinfixture.typeOf
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ThrowingRecursionStrategyTest {

    @Test
    fun `throws illegal state exception when stack is empty`() {
        assertFailsWith<IllegalStateException> {
            ThrowingRecursionStrategy.handleRecursion(typeOf<String>(), emptyList())
        }
    }

    @Test
    fun `throws expected exception when stack is populated`() {
        assertFailsWith<FixtureException> {
            ThrowingRecursionStrategy.handleRecursion(typeOf<String>(), listOf(typeOf<Int>(), typeOf<Float>()))
        }
    }
}
