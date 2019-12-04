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

package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KType

object UnresolvedRecursionStrategy : RecursionStrategy {

    override fun handleRecursion(type: KType, stack: Collection<KType>): Any? {
        check(stack.isNotEmpty()) { "Stack must be populated" }

        return Unresolved.Unsupported(
            "Unable to create ${stack.first()} with circular reference: ${stack.toStackString(type)}"
        )
    }

    private fun Collection<KType>.toStackString(type: KType) = (this + type).joinToString(separator = " → ")
}
