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

package io.github.detomarco.kotlinfixture.resolver

import io.github.detomarco.kotlinfixture.Context
import io.github.detomarco.kotlinfixture.Unresolved
import java.net.URL

internal class UrlResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == URL::class) URL(values.random(context.random)) else Unresolved.Unhandled

    companion object {
        private val values = listOf(
            "https://home.java.net",
            "http://www.oracle.com",
            "http://www.google.com",
            "https://www.github.com",
            "http://www.yahoo.com",
            "http://www.wikipedia.org"
        )
    }
}
