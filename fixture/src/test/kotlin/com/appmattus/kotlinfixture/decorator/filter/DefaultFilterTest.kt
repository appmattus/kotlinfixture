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

package com.appmattus.kotlinfixture.decorator.filter

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.resolver.Resolver
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultFilterTest {
    private val mockObj = mock<Any>()
    private val mockResolver = mock<Resolver>()
    private val mockContext = mock<Context>()

    private val filter = DefaultFilter(mockObj)

    @Test
    fun `iterator calls resolver for values`() {
        whenever(mockResolver.resolve(mockContext, mockObj)) doReturn 5

        val result = filter.next(mockResolver, mockContext)

        verify(mockResolver).resolve(mockContext, mockObj)
        assertEquals(5, result)
    }

    @Test
    fun `calling next with different resolvers calls each resolver once`() {
        whenever(mockResolver.resolve(mockContext, mockObj)) doReturn 5
        filter.next(mockResolver, mockContext)

        val alternateMockResolver = mock<Resolver> {
            on { resolve(mockContext, mockObj) } doReturn 6
        }
        val result = filter.next(alternateMockResolver, mockContext)

        verify(mockResolver, times(1)).resolve(mockContext, mockObj)
        verify(alternateMockResolver, times(1)).resolve(mockContext, mockObj)
        assertEquals(6, result)
    }

    @Test
    fun `chained filter uses supplied resolver`() {
        whenever(mockResolver.resolve(mockContext, mockObj)).thenReturn(1, 2, 3, 4, 5)

        val newFilter = filter.map { filter { (it as Int) % 2 == 0 } }

        val result = newFilter.next(mockResolver, mockContext)

        assertEquals(2, result)
    }

    @Test
    fun `original filter iterator is used by chained filter but not filtered by it`() {
        whenever(mockResolver.resolve(mockContext, mockObj)).thenReturn(1, 2, 3, 4, 5)

        filter.map { filter { (it as Int) % 2 == 0 } }.next(mockResolver, mockContext)

        val result = filter.next(mockResolver, mockContext)
        assertEquals(3, result)
    }

    @Test
    fun `twice chained filter is filtered as expected`() {
        whenever(mockResolver.resolve(mockContext, mockObj)).thenReturn(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val newFilter = filter.map { filter { (it as Int) % 2 == 0 } }.map { filter { (it as Int) % 3 == 0 } }

        val result = newFilter.next(mockResolver, mockContext)

        assertEquals(6, result)
    }
}
