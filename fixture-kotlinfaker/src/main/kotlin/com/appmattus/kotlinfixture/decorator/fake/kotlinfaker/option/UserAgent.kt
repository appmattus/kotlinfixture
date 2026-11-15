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

package com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option

/**
 * Different types of [UserAgent]
 */
@Suppress("unused")
enum class UserAgent(internal val vendor: String?) {
    Any(null),

    Aol("aol"),
    Chrome("chrome"),
    Firefox("firefox"),
    InternetExplorer("internet_explorer"),
    Netscape("netscape"),
    Opera("opera"),
    Safari("safari");

    internal companion object {
        val vendors = entries.mapNotNull { it.vendor }
    }
}
