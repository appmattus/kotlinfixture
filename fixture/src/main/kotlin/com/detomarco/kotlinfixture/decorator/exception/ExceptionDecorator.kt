/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.decorator.exception

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.decorator.Decorator
import com.detomarco.kotlinfixture.resolver.Resolver

internal class ExceptionDecorator : Decorator {
    override fun decorate(resolver: Resolver): Resolver = ExceptionResolver(resolver)

    private class ExceptionResolver(
        private val resolver: Resolver
    ) : Resolver {

        override fun resolve(context: Context, obj: Any): Any? {
            return try {
                resolver.resolve(context, obj)
            } catch (expected: Exception) {
                Unresolved.WithException("Unable to resolve $obj with exception", expected)
            }
        }
    }
}
