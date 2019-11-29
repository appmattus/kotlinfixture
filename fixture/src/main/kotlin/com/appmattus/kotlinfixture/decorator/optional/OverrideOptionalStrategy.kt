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

package com.appmattus.kotlinfixture.decorator.optional

import com.appmattus.kotlinfixture.Context
import kotlin.reflect.KClass

data class OverrideOptionalStrategy(
    private val defaultStrategy: OptionalStrategy,
    private val propertyOverrides: Map<KClass<*>, Map<String, OptionalStrategy>> = emptyMap(),
    private val classOverrides: Map<KClass<*>, OptionalStrategy> = emptyMap()
) : OptionalStrategy {

    @Suppress("ReturnCount")
    override fun Context.generateAsOptional(callingClass: KClass<*>, parameterName: String): Boolean {
        // first check property overrides
        propertyOverrides[callingClass]?.get(parameterName)?.apply {
            return generateAsOptional(callingClass, parameterName)
        }

        // second check class overrides
        classOverrides[callingClass]?.apply {
            return generateAsOptional(callingClass, parameterName)
        }

        // third use default
        with(defaultStrategy) {
            return generateAsOptional(callingClass, parameterName)
        }
    }
}
