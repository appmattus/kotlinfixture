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

import com.appmattus.kotlinfixture.config.Generator
import com.appmattus.kotlinfixture.decorator.fake.FakeStrategy
import net.datafaker.Faker

/**
 * A [Datafaker](https://www.datafaker.net) faker for use in `factory` and `property` generators.
 *
 * The faker uses the locale configured with `dataFakerStrategy`, or the default locale if it has not been configured, and the
 * fixture's random, so its values are reproducible when the fixture is seeded.
 *
 * ```
 * val fixture = kotlinFixture {
 *     dataFakerStrategy()
 *     factory<Person> {
 *         Person(dataFaker.name().firstName(), dataFaker.name().lastName())
 *     }
 * }
 * ```
 */
val Generator<*>.dataFaker: Faker
    get() {
        val configured = fixture.fixtureConfiguration.strategies[FakeStrategy::class] as? DataFakerStrategy
        return (configured ?: DataFakerStrategy()).faker(random)
    }
