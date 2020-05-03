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

package com.appmattus.kotlinfixture.decorator.filter

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.resolver.Resolver
import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

interface Filter {
    val lock: Lock

    var resolver: Resolver
    var context: Context

    val iterator: Iterator<Any?>

    fun next(resolver: Resolver, context: Context): Any? = lock.withLock {
        this.resolver = resolver
        this.context = context
        iterator.next()
    }

    fun map(mapping: Sequence<Any?>.() -> Sequence<Any?>): Filter = DelegatingFilter(this, mapping)
}
