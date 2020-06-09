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

package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.typeOf
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.internal.verification.NoMoreInteractions
import kotlin.reflect.KType
import kotlin.test.Test

class RecursionDecoratorTest {

    private val mockRecursionStrategy = mock<RecursionStrategy>()

    private val config = ConfigurationBuilder().apply {
        recursionStrategy(mockRecursionStrategy)
    }.build()

    @Test
    fun `calls resolver when same KType recursively requested again`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Float>()))

        val decoratedResolver = RecursionDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockRecursionStrategy).handleRecursion(typeOf<Float>(), listOf(typeOf<Float>(), typeOf<Int>()))
    }

    @Test
    fun `does not call resolver when all different KTypes recursively requested`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Double>(), typeOf<Short>()))

        val decoratedResolver = RecursionDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockRecursionStrategy, NoMoreInteractions()).handleRecursion(any(), any())
    }

    class TestResolver(list: List<KType>) : Resolver {

        private val objects = list.iterator()

        override fun resolve(context: Context, obj: Any): Any? {
            return if (objects.hasNext()) {
                context.resolve(objects.next())
            } else {
                obj
            }
        }
    }
}
