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

include(
    "fixture"
)

pluginManagement {

    val detektGradlePluginVersion: String by settings
    val testLoggerVersion: String by settings
    val kotlinVersion: String by settings
    val jreleaserVersion: String by settings
    val dependencyCheckGradlePluginVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("io.gitlab.arturbosch.detekt") version detektGradlePluginVersion
        id("org.jreleaser") version jreleaserVersion
        id("com.adarshr.test-logger") version testLoggerVersion
        id("org.owasp.dependencycheck") version dependencyCheckGradlePluginVersion
        id("signing")
    }

}
