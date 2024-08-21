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

package io.github.detomarco.kotlinfixture.decorator.recursion

import kotlin.reflect.KType

/**
 * Strategy used to determine the return value used when recursion is detected.
 */
interface RecursionStrategy {
    /**
     * Called when recursion is detected. The return value is used to populate the object instead of a generated object.
     * @param type The [type] that resulted in recursion when trying to resolve.
     * @param stack The stack of types resolved before recursion was detected.
     */
    fun handleRecursion(type: KType, stack: Collection<KType>): Any?
}
