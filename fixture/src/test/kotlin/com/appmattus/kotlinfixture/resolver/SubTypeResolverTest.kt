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
import kotlin.test.assertTrue

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

        assertTrue(result is Unresolved)
        verify(context, Times(0)).resolve(any())
    }
}
