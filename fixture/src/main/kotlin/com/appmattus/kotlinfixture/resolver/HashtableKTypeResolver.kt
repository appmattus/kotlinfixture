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
import com.appmattus.kotlinfixture.decorator.nullability.wrapNullability
import java.util.Dictionary
import java.util.Hashtable
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class HashtableKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            val collection = createCollection(obj)

            if (collection != null) {
                return context.wrapNullability(obj) {
                    populateCollection(obj, collection)
                }
            }
        }

        return Unresolved
    }

    private fun Context.populateCollection(obj: KType, collection: Dictionary<Any?, Any?>): Any? {
        val keyType = obj.arguments[0].type!!
        val valueType = obj.arguments[1].type!!

        repeat(configuration.repeatCount()) {
            val key = resolve(keyType)
            val value = resolve(valueType)

            if (key == Unresolved || value == Unresolved) {
                return Unresolved
            }

            collection.put(key, value)
        }

        return collection
    }

    private fun createCollection(obj: KType) = when (obj.classifier as KClass<*>) {

        Dictionary::class,
        Hashtable::class -> Hashtable()

        else -> {
            @Suppress("USELESS_CAST")
            null as Dictionary<Any?, Any?>?
        }
    }
}
