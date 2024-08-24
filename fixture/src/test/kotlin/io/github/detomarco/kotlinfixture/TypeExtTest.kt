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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypeExtTest {
    @Test
    fun `non nullable types convert correctly`() {
        val type = typeOf<List<String>>()

        assertFalse { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable argument convert correctly`() {
        val type = typeOf<List<String?>>()

        assertFalse { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable list convert correctly`() {
        val type = typeOf<List<String>?>()

        assertTrue { type.isMarkedNullable }
        assertFalse { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }

    @Test
    fun `nullable list and argument convert correctly`() {
        val type = typeOf<List<String?>?>()

        assertTrue { type.isMarkedNullable }
        assertTrue { type.arguments.first().type!!.isMarkedNullable }
        assertEquals(List::class, type.classifier)
        assertEquals(String::class, type.arguments.first().type?.classifier)
    }
}
