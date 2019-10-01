package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.internal.verification.Times
import kotlin.test.Test
import kotlin.test.assertEquals

class SubTypeResolverTest {

    val configuration = ConfigurationBuilder().apply {
        subType<Number, Int>()
    }.build()

    val context = mock<Context> { on { this.configuration } doReturn configuration }

    @Test
    fun `sub type mapped when requested`() {
        SubTypeResolver().resolve(context, Number::class)

        verify(context).resolve(Int::class)
    }

    @Test
    fun `Unresolved returned when no mapping found`() {
        val result = SubTypeResolver().resolve(context, String::class)

        assertEquals(Unresolved, result)
        verify(context, Times(0)).resolve(any())
    }
}
