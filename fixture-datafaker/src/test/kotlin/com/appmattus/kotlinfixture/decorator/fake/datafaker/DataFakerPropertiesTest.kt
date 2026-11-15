/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture.decorator.fake.datafaker

import net.datafaker.Faker
import java.util.Date
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class DataFakerPropertiesTest {

    private val configuration = DataFakerConfiguration()

    @Test
    fun `every default property generates a value`() {
        val faker = Faker(configuration.locale, Random(SEED).asJavaRandom())

        configuration.properties.forEach { (propertyName, generator) ->
            repeat(ITERATIONS) {
                val value = try {
                    generator(faker, configuration)
                } catch (expected: Exception) {
                    fail("$propertyName threw $expected")
                }

                when (value) {
                    is String -> assertTrue("$propertyName generated a blank value") { value.isNotBlank() }
                    is Long -> assertTrue("$propertyName generated $value") { value > 0 }
                    is Date -> assertTrue("$propertyName generated $value") { value.before(Date()) }
                    else -> fail("$propertyName generated an unexpected type: $value")
                }
            }
        }
    }

    private companion object {
        private const val SEED = 0
        private const val ITERATIONS = 20
    }
}
