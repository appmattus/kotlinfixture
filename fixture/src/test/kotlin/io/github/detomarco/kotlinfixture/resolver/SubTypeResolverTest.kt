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

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
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
