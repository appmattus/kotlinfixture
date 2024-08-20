/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved
import java.net.URI

internal class UriResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? =
        if (obj == URI::class) URI(values.random(context.random)) else Unresolved.Unhandled

    companion object {
        private val values = listOf(
            "ftp://ftp.is.co.za/rfc/rfc1808.txt",
            "http://www.ietf.org/rfc/rfc2396.txt",
            "telnet://192.0.2.16:80/",
            "mailto:John.Doe@example.com",
            "http://www.wikipedia.org"
        )
    }
}
