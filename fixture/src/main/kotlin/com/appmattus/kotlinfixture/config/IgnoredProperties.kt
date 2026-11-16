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

package com.appmattus.kotlinfixture.config

import kotlin.reflect.KClass

/**
 * Properties that are not populated after an instance is created, set through `ignoreProperty` and
 * `ignoreLateinitProperties` on [ConfigurationBuilder]. Stored in [Configuration.strategies].
 */
internal data class IgnoredProperties(
    val properties: Map<KClass<*>, Set<String>> = emptyMap(),
    val lateinit: Boolean = false
) {
    fun isIgnored(clazz: KClass<*>, propertyName: String): Boolean =
        properties[clazz]?.contains(propertyName) == true
}
