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

package io.github.detomarco.kotlinfixture.decorator.nullability

import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.resolver.CompositeResolver
import io.github.detomarco.kotlinfixture.typeOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AlwaysNullStrategyTest {

    private val context = TestContext(
        configuration = ConfigurationBuilder().apply {
            nullabilityStrategy(AlwaysNullStrategy)
        }.build(),
        resolver = CompositeResolver()
    )

    @Test
    fun `AlwaysNullStrategy is always true`() = with(context) {
        repeat(100) {
            with(AlwaysNullStrategy) {
                assertTrue {
                    generateAsNull()
                }
            }
        }
    }

    @Test
    fun `always null`() {
        repeat(100) {
            assertNull(
                context.wrapNullability(typeOf<String?>()) {
                    "value"
                }
            )
        }
    }
}
