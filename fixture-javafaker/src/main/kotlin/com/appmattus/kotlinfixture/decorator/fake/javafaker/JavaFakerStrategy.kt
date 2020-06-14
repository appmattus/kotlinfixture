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

package com.appmattus.kotlinfixture.decorator.fake.javafaker

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.decorator.fake.FakeStrategy
import com.github.javafaker.Faker
import kotlin.random.asJavaRandom

internal class JavaFakerStrategy(
    private val fakerConfiguration: JavaFakerConfiguration = JavaFakerConfigurationBuilder(
        JavaFakerConfiguration()
    ).build()
) : FakeStrategy {

    override fun fake(context: Context, propertyName: String): Any? {
        val func = fakerConfiguration.properties.getOrElse(propertyName) { { Unresolved.Unhandled } }
        return func(Faker(fakerConfiguration.locale, context.random.asJavaRandom()), fakerConfiguration)
    }

    fun new(configuration: JavaFakerConfigurationBuilder.() -> Unit = {}) =
        JavaFakerStrategy(
            JavaFakerConfigurationBuilder(
                fakerConfiguration
            ).apply(configuration).build()
        )
}
