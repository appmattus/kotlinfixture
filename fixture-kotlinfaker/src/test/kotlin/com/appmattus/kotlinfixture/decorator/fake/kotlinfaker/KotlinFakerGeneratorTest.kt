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

package com.appmattus.kotlinfixture.decorator.fake.kotlinfaker

import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.kotlinFixture
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.FakerConfig
import org.junit.Test
import java.util.Locale
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.test.assertEquals

class KotlinFakerGeneratorTest {

    data class Person(val name: String)

    @Test
    fun `kotlinFaker in a factory uses the configured locale and the fixture's random`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            kotlinFakerStrategy {
                locale = JAPANESE
            }
            factory<Person> { Person(kotlinFaker.name.firstName()) }
        }

        val expected = Faker(
            FakerConfig.builder().withLocale(JAPANESE.toLanguageTag()).withRandom(Random(SEED).asJavaRandom()).build()
        ).name.firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `kotlinFaker in a property generator uses the configured locale and the fixture's random`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            kotlinFakerStrategy {
                locale = JAPANESE
            }
            property<Person, String>("name") { kotlinFaker.name.firstName() }
        }

        val expected = Faker(
            FakerConfig.builder().withLocale(JAPANESE.toLanguageTag()).withRandom(Random(SEED).asJavaRandom()).build()
        ).name.firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `kotlinFaker uses the default locale when kotlinFakerStrategy is not configured`() {
        val fixture = kotlinFixture {
            random = Random(SEED)
            factory<Person> { Person(kotlinFaker.name.firstName()) }
        }

        val expected = Faker(
            FakerConfig.builder().withLocale(
                Locale.ENGLISH.toLanguageTag()
            ).withRandom(Random(SEED).asJavaRandom()).build()
        ).name.firstName()

        assertEquals(expected, fixture<Person>().name)
    }

    @Test
    fun `kotlinFaker is random when the fixture is not seeded`() {
        val fixture = kotlinFixture {
            factory<Person> { Person(kotlinFaker.name.firstName()) }
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
