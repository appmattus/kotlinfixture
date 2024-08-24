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
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SubTypeResolverTest {

    val configuration = ConfigurationBuilder().apply {
        subType<Number, Int>()
    }.build()

    val context = mockk<Context>()

    @Test
    fun `sub type mapped when requested`() {
        every { context.configuration } returns configuration
        every { context.resolve(Int::class) } returns 1

        SubTypeResolver().resolve(context, Number::class)

        verify { context.resolve(Int::class) }
    }

    @Test
    fun `Unresolved returned when no mapping found`() {
        every { context.configuration } returns configuration

        val result = SubTypeResolver().resolve(context, String::class)

        (result is Unresolved).shouldBeTrue()
        verify(exactly = 0) { context.resolve(any()) }
    }
}
