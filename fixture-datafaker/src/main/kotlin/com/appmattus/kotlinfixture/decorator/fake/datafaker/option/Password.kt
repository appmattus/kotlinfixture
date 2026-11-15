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

package com.appmattus.kotlinfixture.decorator.fake.datafaker.option

/**
 * Configuration for password generation
 *
 * Datafaker includes at least one character of each enabled type, so [maximumLength] must be at least the number of
 * enabled options out of [includeUppercase], [includeSpecial] and [includeDigit].
 */
data class Password(
    val minimumLength: Int = 8,
    val maximumLength: Int = 16,
    val includeUppercase: Boolean = true,
    val includeSpecial: Boolean = true,
    val includeDigit: Boolean = true
)
