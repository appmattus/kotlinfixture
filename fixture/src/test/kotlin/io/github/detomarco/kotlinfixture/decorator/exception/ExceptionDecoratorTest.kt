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

package io.github.detomarco.kotlinfixture.decorator.exception

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.resolver.Resolver
import io.github.detomarco.kotlinfixture.typeOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExceptionDecoratorTest {

    private val config = ConfigurationBuilder().build()

    @Test
    fun `returns expected value when no exception thrown in resolver`() {
        val resolver = TestResolver(sequenceOf(0.3f))
        val decoratedResolver = ExceptionDecorator().decorate(resolver)

        val result = decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        assertTrue {
            result is Float
        }
    }

    @Test
    fun `returns unresolved when exception thrown in resolver`() {
        val resolver = TestResolver(generateSequence { throw UnsupportedOperationException("Oh no") })

        val decoratedResolver = ExceptionDecorator().decorate(resolver)

        val result = decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        assertTrue {
            (result as? Unresolved.WithException)?.exception?.message?.contains("Oh no") == true
        }
    }

    private class TestResolver(list: Sequence<Any?>) : Resolver {

        private val iterator = list.iterator()

        override fun resolve(context: Context, obj: Any) = iterator.next()
    }
}
