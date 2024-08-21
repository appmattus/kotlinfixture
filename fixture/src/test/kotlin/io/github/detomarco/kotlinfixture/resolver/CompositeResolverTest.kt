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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.config.Configuration
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompositeResolverTest {

    private val unresolvedResolver1 = mock<Resolver> {
        on { resolve(any(), any()) } doReturn Unresolved.Unhandled
    }

    private val unresolvedResolver2 = mock<Resolver> {
        on { resolve(any(), any()) } doReturn Unresolved.Unhandled
    }

    private val resolvedResolver1 = mock<Resolver> {
        on { resolve(any(), any()) } doAnswer { it.getArgument<Any>(1) }
    }

    @Test
    fun `Calls all resolvers when unresolved`() {
        val compositeResolver = CompositeResolver(unresolvedResolver1, unresolvedResolver2)
        val context = TestContext(Configuration(), compositeResolver)

        val result = context.resolve(Number::class)

        verify(unresolvedResolver1).resolve(any(), any())
        verify(unresolvedResolver2).resolve(any(), any())
        assertTrue(result is Unresolved)
    }

    @Test
    fun `Calls resolvers until resolvable`() {
        val compositeResolver = CompositeResolver(unresolvedResolver1, resolvedResolver1, unresolvedResolver2)
        val context = TestContext(Configuration(), compositeResolver)

        val result = context.resolve(Number::class)

        verify(unresolvedResolver1).resolve(any(), any())
        verify(resolvedResolver1).resolve(any(), any())
        verifyNoMoreInteractions(unresolvedResolver2)
        assertEquals(Number::class, result)
    }

    @Test
    fun `Iterates over resolvers in order`() {
        val compositeResolver = CompositeResolver(unresolvedResolver1, resolvedResolver1, unresolvedResolver2)

        assertEquals(listOf(unresolvedResolver1, resolvedResolver1, unresolvedResolver2), compositeResolver.toList())
    }
}
