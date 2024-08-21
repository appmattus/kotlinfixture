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

package io.github.detomarco.kotlinfixture.decorator.constructor

import io.github.detomarco.kotlinfixture.ContextImpl
import io.github.detomarco.kotlinfixture.config.Configuration
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayFavouringConstructorStrategyTest {

    @Test
    fun `Order constructors with greatest array parameter count first followed by remaining modest first`() {
        val context = ContextImpl(Configuration())

        val shuffledConstructors = mock<KClass<MultipleConstructors>> {
            on { constructors } doReturn MultipleConstructors::class.constructors.shuffled()
        }

        val result = ArrayFavouringConstructorStrategy.constructors(context, shuffledConstructors).map {
            val emptyParameters = List<Any?>(it.parameters.size) { null }
            (it.call(*emptyParameters.toTypedArray()) as MultipleConstructors).constructorCalled
        }

        assertEquals(listOf("array-2", "array-1", "primary", "string"), result)
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    class MultipleConstructors {
        val constructorCalled: String

        constructor() {
            constructorCalled = "primary"
        }

        constructor(array: Array<String>?) {
            constructorCalled = "array-1"
        }

        constructor(value: String?) {
            constructorCalled = "string"
        }

        constructor(array1: Array<String>?, array2: BooleanArray?) {
            constructorCalled = "array-2"
        }
    }
}
