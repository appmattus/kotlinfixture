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
import com.nhaarman.mockitokotlin2.mock
import java.util.concurrent.locks.Lock
import kotlin.test.Test
import kotlin.test.assertEquals

class DelegatingFilterTest {

    private val mockLock = mock<Lock>()
    private val mockResolver = mock<Resolver>()
    private val mockResolverAlternate = mock<Resolver>()
    private val mockContext = mock<Context>()
    private val mockContextAlternate = mock<Context>()

    private val delegateFilter = object : Filter {
        override val lock = mockLock
        override val iterator = (1..10).iterator()
        override var resolver: Resolver = mockResolver
        override var context: Context = mockContext
    }

    private val filter = DelegatingFilter(delegateFilter) { this }

    @Test
    fun `getLock delegates`() {
        assertEquals(mockLock, filter.lock)
    }

    @Test
    fun `getResolver delegates`() {
        assertEquals(mockResolver, filter.resolver)
    }

    @Test
    fun `setResolver delegates`() {
        filter.resolver = mockResolverAlternate

        assertEquals(mockResolverAlternate, delegateFilter.resolver)
    }

    @Test
    fun `getContext delegates`() {
        assertEquals(mockContext, filter.context)
    }

    @Test
    fun `setContext delegates`() {
        filter.context = mockContextAlternate

        assertEquals(mockContextAlternate, delegateFilter.context)
    }

    @Test
    fun `no filter applied returns the original iterator`() {
        assertEquals(delegateFilter.iterator, filter.iterator)
    }

    @Test
    fun `filter applies to the iterator`() {
        val filter = DelegatingFilter(delegateFilter) { filter { (it as Int) > 2 } }

        assertEquals(3, filter.iterator.next())
    }

    @Test
    fun `chaining applies all filters to the iterator`() {
        val filter = DelegatingFilter(
            DelegatingFilter(delegateFilter) { filter { (it as Int) > 2 } }
        ) {
            filter { (it as Int) % 2 == 0 }
        }

        assertEquals(4, filter.iterator.next())
    }
}
