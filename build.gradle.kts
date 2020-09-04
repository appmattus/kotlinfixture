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

import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    kotlin("jvm") version "1.4.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.12.0"
    id("com.appmattus.markdown") version "0.6.0"
    id("org.jetbrains.dokka") version "1.4.0"
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}

apply(from = "$rootDir/gradle/scripts/dependencyUpdates.gradle.kts")
apply(from = "$rootDir/owaspDependencyCheck.gradle.kts")

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }

    tasks.withType<DokkaTask> {
        outputDirectory.set(buildDir.resolve("reports/dokka"))

        dokkaSourceSets {
            configureEach {
                skipDeprecated.set(true)

                sourceLink {
                    localDirectory.set(rootDir)
                    remoteUrl.set(URL("https://github.com/appmattus/kotlinfixture/blob/main/"))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.12.0")
}

detekt {
    input = files("$projectDir")

    buildUponDefaultConfig = true

    autoCorrect = true

    config = files("detekt-config.yml")
}

val dokka = tasks.named<DokkaMultiModuleTask>("dokkaHtmlMultiModule") {
    outputDirectory.set(buildDir.resolve("dokkaCustomMultiModuleOutput"))
    documentationFileName.set("module.md")
}

tasks.register("check").dependsOn(dokka)
