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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListExtTest {
    @Test
    fun `circular iterator loops`() {
        val list = listOf("A", "B", "C").circularIterator().asSequence().take(6).toList()

        assertEquals(list.take(3), list.takeLast(3))
    }

    @Test
    fun `empty list has no next`() {
        val result = emptyList<String>().circularIterator().hasNext()

        assertFalse(result)
    }

    @Test
    fun `empty list throws exception when getting next value`() {
        assertFailsWith(NoSuchElementException::class) {
            emptyList<String>().circularIterator().next()
        }
    }

    @Test
    fun `list with content has no next`() {
        val result = listOf("A").circularIterator().hasNext()

        assertTrue(result)
    }
}
