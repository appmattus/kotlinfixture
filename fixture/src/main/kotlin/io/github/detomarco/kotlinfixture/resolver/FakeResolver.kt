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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.Unresolved
import io.github.detomarco.kotlinfixture.decorator.fake.FakeStrategy
import io.github.detomarco.kotlinfixture.decorator.fake.NoFakeStrategy
import io.github.detomarco.kotlinfixture.strategyOrDefault

internal class FakeResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any {
        if (obj is KNamedPropertyRequest) {
            val strategy = context.strategyOrDefault<FakeStrategy>(NoFakeStrategy)

            val overrides = context.configuration.properties.getOrElse(obj.containingClass) { emptyMap() }

            if (obj.name != null && !overrides.containsKey(obj.name)) {
                strategy.fake(context, obj.name)?.takeIf { it != Unresolved.Unhandled }?.let {
                    return it
                }
            }
        }

        return Unresolved.Unhandled
    }
}
