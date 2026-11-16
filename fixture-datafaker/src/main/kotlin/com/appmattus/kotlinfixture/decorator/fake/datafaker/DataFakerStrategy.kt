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

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.decorator.fake.FakeStrategy
import net.datafaker.Faker
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class DataFakerStrategy(
    private val fakerConfiguration: DataFakerConfiguration = DataFakerConfigurationBuilder(
        DataFakerConfiguration()
    ).build()
) : FakeStrategy {

    override fun fake(context: Context, propertyName: String): Any? {
        val func = fakerConfiguration.properties.getOrElse(propertyName) { { Unresolved.Unhandled } }
        return func(faker(context.random), fakerConfiguration)
    }

    /** A faker using the configured locale and the given [random], so its values are reproducible when seeded */
    fun faker(random: Random): Faker = Faker(fakerConfiguration.locale, random.asJavaRandom())

    fun new(configuration: DataFakerConfigurationBuilder.() -> Unit = {}) =
        DataFakerStrategy(
            DataFakerConfigurationBuilder(
                fakerConfiguration
            ).apply(configuration).build()
        )
}
