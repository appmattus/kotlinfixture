/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.decorator.fake

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved

/**
 * Strategy used to provide fake data for named properties, useful if you need to generate objects with pretty data.
 */
interface FakeStrategy {
    /**
     * Return pretty data for a named property, [propertyName], or [Unresolved.Unhandled] if no appropriate value can be
     * generated. [context] should be used to access `random`, or to `resolve` any types through the resolver chain.
     */
    fun fake(context: Context, propertyName: String): Any?
}
