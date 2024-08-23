/*
 * Copyright 2021-2023 Appmattus Limited
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

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jreleaser.model.Active

plugins {
    kotlin("jvm") apply false
    id("io.gitlab.arturbosch.detekt")
    id("org.jreleaser")
    id("signing")
    id("com.adarshr.test-logger")
    id("org.owasp.dependencycheck")
}

val detektGradlePluginVersion: String by project

allprojects {

    apply {
        plugin("com.adarshr.test-logger")
        plugin("io.gitlab.arturbosch.detekt")
    }
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektGradlePluginVersion")
    }
    group = "io.github.detomarco.kotlinfixture"
    version = (System.getenv("GITHUB_REF") ?: System.getProperty("GITHUB_REF"))
        ?.replaceFirst("refs/tags/", "") ?: "unspecified"

    testlogger {
        theme = ThemeType.MOCHA
        showSimpleNames = true
    }

    detekt {
        allRules = true
        buildUponDefaultConfig = true
        autoCorrect = System.getProperty("autoCorrect") == "true"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektGradlePluginVersion")
}



jreleaser {
    project {
        license = "APACHE-2.0"
        authors = listOf("Appmattus", "detomarco")
        copyright = "2019-2023 Appmattus, 2024 detomarco"
        description = "Fixtures for Kotlin providing generated values for unit testing"
    }
    signing {
        active = Active.ALWAYS
        armored = true
    }

    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepositories.add("fixture/build/staging-deploy")
                    stagingRepositories.add("fixture-generex/build/staging-deploy")
                    stagingRepositories.add("fixture-javafaker/build/staging-deploy")
                    stagingRepositories.add("fixture-kotest/build/staging-deploy")
                    retryDelay = 60
                    maxRetries = 100
                }
            }

        }
    }
}



dependencyCheck {
    nvd.apiKey = System.getenv("NVD_API_KEY")
    failBuildOnCVSS = 0f
    suppressionFile = "cve-suppressions.xml"
    autoUpdate = true
    // Disable the .NET Assembly Analyzer. Requires an external tool, and this project likely won't ever have .NET DLLs.
    analyzers.assemblyEnabled = false
}


