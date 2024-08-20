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

package com.detomarco.kotlinfixture.decorator.nullability

import com.detomarco.kotlinfixture.TestContext
import com.detomarco.kotlinfixture.assertIsRandom
import com.detomarco.kotlinfixture.config.Configuration
import com.detomarco.kotlinfixture.resolver.CompositeResolver
import com.detomarco.kotlinfixture.typeOf
import kotlin.test.Test

class NullabilityCheckTest {

    private val context = TestContext(Configuration(), CompositeResolver())

    @Test
    fun `default wrapNullability is randomly null`() {
        assertIsRandom {
            context.wrapNullability(typeOf<String?>()) {
                "value"
            } == null
        }
    }
}
