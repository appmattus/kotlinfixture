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

package com.appmattus.kotlinfixture.decorator.constructor

import com.appmattus.kotlinfixture.ContextImpl
import com.appmattus.kotlinfixture.config.Configuration
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals

class ListFavouringConstructorStrategyTest {
    @Test
    fun `Order constructors with greatest list parameter count first followed by remaining modest first`() {
        val context = ContextImpl(Configuration())

        val shuffledConstructors = mock<KClass<MultipleConstructors>> {
            on { constructors } doReturn MultipleConstructors::class.constructors.shuffled()
        }

        val result = ListFavouringConstructorStrategy.constructors(context, shuffledConstructors).map {
            val emptyParameters = List<Any?>(it.parameters.size) { null }
            (it.call(*emptyParameters.toTypedArray()) as MultipleConstructors).constructorCalled
        }

        assertEquals(listOf("list-2", "list-1", "primary", "string"), result)
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    class MultipleConstructors {
        val constructorCalled: String

        constructor() {
            constructorCalled = "primary"
        }

        constructor(list: List<String>?) {
            constructorCalled = "list-1"
        }

        constructor(value: String?) {
            constructorCalled = "string"
        }

        constructor(list1: List<String>?, list2: List<Boolean>?) {
            constructorCalled = "list-2"
        }
    }
}
