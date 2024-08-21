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

package io.github.detomarco.kotlinfixture.decorator.logging

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.resolver.Resolver
import io.github.detomarco.kotlinfixture.typeOf
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.internal.verification.Times
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggingDecoratorTest {

    private val mockLoggingStrategy = mock<LoggingStrategy>()

    private val config = ConfigurationBuilder().apply {
        loggingStrategy(mockLoggingStrategy)
    }.build()

    @Test
    fun `logs each request`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Short>()))

        val decoratedResolver = LoggingDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        argumentCaptor<KType> {
            verify(mockLoggingStrategy, Times(3)).request(capture())

            assertEquals(listOf(typeOf<Float>(), typeOf<Int>(), typeOf<Short>()), allValues)
        }
    }

    @Test
    fun `logs each response`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Short>()))

        val decoratedResolver = LoggingDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockLoggingStrategy).response(typeOf<Short>(), Result.success(typeOf<Short>()))
        verify(mockLoggingStrategy).response(typeOf<Int>(), Result.success(typeOf<Short>()))
        verify(mockLoggingStrategy).response(typeOf<Float>(), Result.success(typeOf<Short>()))
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
