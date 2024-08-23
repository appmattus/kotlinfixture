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

package io.github.detomarco.kotlinfixture.datafaker.option

import net.datafaker.providers.base.Internet

/**
 * Different types of [UserAgent]
 */
@Suppress("unused")
enum class UserAgent(internal val userAgent: Internet.UserAgent?) {
    Any(null),

    Aol(Internet.UserAgent.AOL),
    Chrome(Internet.UserAgent.CHROME),
    Firefox(Internet.UserAgent.FIREFOX),
    InternetExplorer(Internet.UserAgent.INTERNET_EXPLORER),
    Netscape(Internet.UserAgent.NETSCAPE),
    Opera(Internet.UserAgent.OPERA),
    Safari(Internet.UserAgent.SAFARI)
}
