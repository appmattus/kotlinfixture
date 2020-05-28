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

package com.appmattus.kotlinfixture.decorator.fake

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.decorator.Decorator
import com.appmattus.kotlinfixture.resolver.KNamedPropertyRequest
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.strategyOrDefault
import kotlin.reflect.KClass

internal class FakeDecorator : Decorator {

    override fun decorate(resolver: Resolver): Resolver = FakeResolver(resolver)

    private class FakeResolver(
        private val resolver: Resolver
    ) : Resolver {

        override fun resolve(context: Context, obj: Any): Any? {

            val strategy = context.strategyOrDefault<FakeStrategy>(NoFakeStrategy)

            if (obj is KNamedPropertyRequest) {
                val overrides = context.configuration.properties.getOrElse(obj.containingClass) { emptyMap() }

                if (obj.name != null && !overrides.containsKey(obj.name)) {
                    strategy.fake(context, obj.name)?.takeIf { it != Unresolved.Unhandled }?.let {

                        println(it::class)
                        println(obj.type)
                        println((obj.type.classifier as KClass<*>).isInstance(it))

                        return it
                    }
                }
            }

            return resolver.resolve(context, obj)
        }
    }
}
