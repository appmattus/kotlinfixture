package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlin.test.Test
import kotlin.test.assertEquals

class CompositeResolverTest {

    private val unresolvedResolver1 = mock<Resolver> {
        on { resolve(any(), any()) } doReturn Unresolved
    }

    private val unresolvedResolver2 = mock<Resolver> {
        on { resolve(any(), any()) } doReturn Unresolved
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
        assertEquals(Unresolved, result)
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
