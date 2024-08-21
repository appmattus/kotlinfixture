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

package io.github.detomarco.kotlinfixture.decorator.filter

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.resolver.Resolver
import java.util.concurrent.locks.ReentrantLock

internal class DefaultFilter(private val obj: Any) : Filter {
    override val lock = ReentrantLock()

    override lateinit var resolver: Resolver
    override lateinit var context: Context

    override val iterator = sequence {
        while (true) {
            yield(resolver.resolve(context, obj))
        }
    }.iterator()
}
