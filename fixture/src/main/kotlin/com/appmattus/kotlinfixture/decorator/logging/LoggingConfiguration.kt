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

package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.config.ConfigurationBuilder

/**
 * # Logging object generation with `loggingStrategy`
 *
 * A basic logger can be applied using the built in [SysOutLoggingStrategy]. It is also possible to define and implement your own logging strategy by implementing [LoggingStrategy] and applying it as below.
 *
 * ```
 * val fixture = kotlinFixture {
 *     loggingStrategy(SysOutLoggingStrategy)
 * }
 * ```
 *
 * The logger for `fixture<String>()` outputs:
 *
 * ```
 * ktype kotlin.String →
 *     class kotlin.String →
 *         Success(5878ec34-c30f-40c7-ad52-c15a39b44ac1)
 *     Success(5878ec34-c30f-40c7-ad52-c15a39b44ac1)
 * ```
 */
@Suppress("unused")
fun ConfigurationBuilder.loggingStrategy(strategy: LoggingStrategy) {
    strategies[LoggingStrategy::class] = strategy
}
