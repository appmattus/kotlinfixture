/*
 * Copyright 2021-2023 Appmattus Limited
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

package io.github.detomarco.kotlinfixture

import com.flextrade.kfixture.KFixture
import com.marcellogalhardo.fixture.Fixture
import org.jeasy.random.EasyRandom
import kotlin.reflect.KClass
import kotlin.test.Test

class ComparisonSingleTest {

    private val nullableType = typeOf<String?>()

    @Test
    fun `detomarco nullability supported`() {
        assertIsRandom {
            @Suppress("DEPRECATION_ERROR")
            val result = detomarco.create(nullableType, detomarco.fixtureConfiguration)

            result == null
        }
    }

    @Test
    fun `flextrade nullability unsupported`() {
        assertNone {
            val result = flextrade.jFixture.create((nullableType.classifier as KClass<*>).java)

            result == null
        }
    }

    @Test
    fun `marcellogalhardo nullability unsupported`() {
        assertNone {
            val result = marcellogalhardo.next(nullableType.classifier as KClass<*>, nullableType)

            result == null
        }
    }

    @Test
    fun `easyrandom nullability unsupported`() {
        assertNone {
            val result = easyRandom.nextObject((nullableType.classifier as KClass<*>).java)

            result == null
        }
    }

    private companion object {
        private val marcellogalhardo = Fixture()
        private val flextrade = KFixture()
        private val detomarco = kotlinFixture()
        private val easyRandom = EasyRandom()
    }
}
