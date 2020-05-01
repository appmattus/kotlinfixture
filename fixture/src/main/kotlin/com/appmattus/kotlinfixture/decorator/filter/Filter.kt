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

interface Filter {
    val iterator: Iterator<Any?>
    var resolver: Resolver
    var context: Context

    fun next(resolver: Resolver, context: Context): Any? {
        this.resolver = resolver
        this.context = context
        return iterator.next()
    }

    fun map(mapping: Sequence<Any?>.() -> Sequence<Any?>): Filter {
        val newIterator = mapping(iterator.asSequence()).iterator()
        return object : Filter by this {

            override fun next(resolver: Resolver, context: Context): Any? {
                this.resolver = resolver
                this.context = context
                return newIterator.next()
            }
        }
    }
}
