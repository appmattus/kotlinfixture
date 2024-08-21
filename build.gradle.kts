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
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jreleaser.model.Active
import java.net.URI

plugins {
    kotlin("jvm") apply false
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    id("org.jreleaser")
    id("signing")
    id("com.adarshr.test-logger")
    id("org.owasp.dependencycheck")
}

val detektGradlePluginVersion: String by project

allprojects {
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    group = "io.github.detomarco"
//    version = (System.getenv("GITHUB_REF") ?: System.getProperty("GITHUB_REF"))
//        ?.replaceFirst("refs/tags/", "") ?: "unspecified"

    version = "0.0.2"

    plugins.withType<DokkaPlugin> {
        tasks.withType<DokkaTask>().configureEach {
            dokkaSourceSets {
                configureEach {
                    skipDeprecated.set(true)

                    sourceLink {
                        localDirectory.set(rootDir)
                        remoteUrl.set(URI("https://github.com/detomarco/kotlinfixture/blob/main").toURL())
                        remoteLineSuffix.set("#L")
                    }
                }
            }
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektGradlePluginVersion")
}

detekt {
    source = files(fileTree(projectDir).matching {
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
    }.files)

    buildUponDefaultConfig = true

    autoCorrect = true

    config = files("detekt-config.yml")
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
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepositories.add("fixture/build/staging-deploy")
                }
            }
        }
    }
}

testlogger {
    theme = ThemeType.MOCHA
    showSimpleNames = true
}

dependencyCheck {
    failBuildOnCVSS = 0f
    suppressionFile = "project-suppression.xml"
    autoUpdate = System.getProperty("dependencyCheckAutoUpdate")?.toString()?.trim()?.lowercase() != "false"
    // Disable the .NET Assembly Analyzer. Requires an external tool, and this project likely won't ever have .NET DLLs.
    analyzers.assemblyEnabled = false
}
