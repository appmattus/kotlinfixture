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

import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.kotlinFixture
import net.datafaker.Faker
import org.junit.Test
import java.util.Locale
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.test.assertEquals

class DataFakerGeneratorTest {

    data class Person(val name: String)

    @Test
    fun `dataFaker in a factory uses the configured locale and the fixture's random`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            dataFakerStrategy {
                locale = JAPANESE
            }
            factory<Person> { Person(dataFaker.name().firstName()) }
        }

        val expected = Faker(JAPANESE, Random(SEED).asJavaRandom()).name().firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `dataFaker in a property generator uses the configured locale and the fixture's random`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            dataFakerStrategy {
                locale = JAPANESE
            }
            property<Person, String>("name") { dataFaker.name().firstName() }
        }

        val expected = Faker(JAPANESE, Random(SEED).asJavaRandom()).name().firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `dataFaker uses the default locale when dataFakerStrategy is not configured`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            factory<Person> { Person(dataFaker.name().firstName()) }
        }

        val expected = Faker(Locale.ENGLISH, Random(SEED).asJavaRandom()).name().firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `dataFaker is random when the fixture is not seeded`() {
        val fixture = kotlinFixture {
            factory<Person> { Person(dataFaker.name().firstName()) }
        }

        assertIsRandom {
            fixture<Person>().name
        }
    }

    private companion object {
        private const val SEED = 42
        private val JAPANESE = Locale.forLanguageTag("ja")
    }
}
