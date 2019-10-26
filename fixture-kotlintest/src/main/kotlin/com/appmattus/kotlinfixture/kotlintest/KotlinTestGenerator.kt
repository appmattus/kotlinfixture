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

package com.appmattus.kotlinfixture.kotlintest

import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import io.kotlintest.properties.Gen
import kotlin.reflect.KType

class KotlinTestGenerator(private val kotlinFixture: Fixture, private val type: KType) : Gen<Any?> {
    private val configuration = ConfigurationBuilder(kotlinFixture.fixtureConfiguration).apply {
        resolvers.add(0, KotlinTestResolver())
    }.build()

    override fun constants() = emptyList<Any?>()
    override fun random() = generateSequence {
        kotlinFixture.create(type, configuration)
    }
}
