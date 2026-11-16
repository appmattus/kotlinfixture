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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.circularIterator
import java.util.WeakHashMap
import kotlin.random.Random
import kotlin.reflect.KClass

internal class EnumResolver : Resolver {

    // Values cycle in a shuffled order so each is generated. The order is kept per Random, so a seeded Random always
    // produces the same values, including when a Random is set for a single call. Held weakly so unused Random
    // instances can be garbage collected.
    private val cache = WeakHashMap<Random, MutableMap<KClass<*>, Iterator<*>>>()

    override fun resolve(context: Context, obj: Any): Any? {
        if ((obj as? KClass<*>)?.java?.isEnum == true && obj.java.enumConstants?.isNotEmpty() == true) {
            return cache.getOrPut(context.random) { mutableMapOf() }.getOrPut(obj) {
                obj.java.enumConstants!!.toList().shuffled(context.random).circularIterator()
            }.next()
        }

        return Unresolved.Unhandled
    }
}
