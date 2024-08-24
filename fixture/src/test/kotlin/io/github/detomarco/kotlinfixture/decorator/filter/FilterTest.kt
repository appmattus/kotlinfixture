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

package io.github.detomarco.kotlinfixture.decorator.filter

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.resolver.Resolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.concurrent.locks.Lock

class FilterTest {
    private val mockLock = mock<Lock>()
    private val mockResolver = mock<Resolver>()
    private val mockContext = mock<Context>()
    private val mockIterator = mock<Iterator<Int>> {
        on { hasNext() } doReturn true
        onGeneric { next() }.doReturn(1, 2, 3, 4, 5)
    }

    abstract class FilterImpl : Filter

    private val mockFilter = mock<FilterImpl> {
        on { lock } doReturn mockLock

        on { resolver } doReturn mockResolver
        on { context } doReturn mockContext

        on { iterator } doReturn mockIterator

        on { next(any(), any()) }.thenCallRealMethod()
        on { map(any()) }.thenCallRealMethod()
    }

    @Test
    fun `next locks and sets resolver and context before calling the iterator and unlocking`() {
        mockFilter.next(mock(), mock())

        inOrder(mockLock, mockFilter, mockIterator) {
            verify(mockLock).lock()
            verify(mockFilter).resolver = any()
            verify(mockFilter).context = any()
            verify(mockIterator).next()
            verify(mockLock).unlock()
        }

        println(mockIterator.next())
        println(mockIterator.next())
    }

    @Test
    fun `mapping creates new filter with filtering applied to the iterator`() {
        val newFilter = mockFilter.map { filter { (it as Int) % 2 == 0 } }

        assertEquals(2, newFilter.iterator.next())
        assertEquals(4, newFilter.iterator.next())
    }

    @Test
    fun `mapping moves the original iterator`() {
        val newFilter = mockFilter.map { filter { (it as Int) % 2 == 0 } }

        newFilter.iterator.next()

        verify(mockIterator, times(2)).next()
    }
}
