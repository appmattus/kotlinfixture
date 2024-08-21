/*
 * Copyright 2021 Appmattus Limited
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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.TestContext
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.assertIsRandom
import io.github.detomarco.kotlinfixture.config.Configuration
import io.github.detomarco.kotlinfixture.typeOf
import org.ktorm.entity.Entity
import kotlin.test.Test
import kotlin.test.assertTrue

class KTormResolverTest {

    val context = TestContext(
        Configuration(),
        CompositeResolver(KTormResolver(), StringResolver(), PrimitiveResolver(), KTypeResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertTrue(result is Unresolved)
    }

    @Test
    fun `Random nullability returned`() {
        assertIsRandom {
            context.resolve(typeOf<TestEntity?>()) == null
        }
    }

    @Test
    fun `Entity populated with random ids`() {
        assertIsRandom {
            (context.resolve(typeOf<TestEntity>()) as TestEntity).id
        }
    }

    @Test
    fun `Entity populated with random names`() {
        assertIsRandom {
            (context.resolve(typeOf<TestEntity>()) as TestEntity).name
        }
    }

    @Test
    fun `Entity class returns Unresolved`() {
        val result = context.resolve(typeOf<Entity<TestEntity>>())

        assertTrue(result is Unresolved)
    }

    interface TestEntity : Entity<TestEntity> {
        companion object : Entity.Factory<TestEntity>()

        val id: Int
        var name: String
    }
}
