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

package com.appmattus.kotlinfixture.decorator.constructor

import com.appmattus.kotlinfixture.Context
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.starProjectedType

/**
 * Order constructors, selecting those with the most parameters matching List<*> before any other. Any other
 * constructors are returned with the most modest constructors first.
 */
object ListFavouringConstructorStrategy : ConstructorStrategy {
    override fun constructors(context: Context, obj: KClass<*>): Collection<KFunction<*>> {
        return ModestConstructorStrategy.constructors(context, obj).sortedByDescending {
            it.parameters.count { parameter ->
                (parameter.type.classifier as KClass<*>).starProjectedType == List::class.starProjectedType
            }
        }
    }
}
