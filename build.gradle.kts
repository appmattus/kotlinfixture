/*
 * Copyright 2019 Appmattus Limited
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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version "0.27.0"
    id("io.gitlab.arturbosch.detekt") version "1.1.1"
    id("com.appmattus.markdown") version "0.6.0"
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.60")
        classpath("com.android.tools.build:gradle:3.5.2")
    }
}

apply(from = "$rootDir/owaspDependencyCheck.gradle.kts")

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(DependencyUpdatesTask::class.java).all {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                    .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                    .any { it.matches(candidate.version) }

                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.1.1")
}

detekt {
    input = files("$projectDir")

    buildUponDefaultConfig = true

    config = files("detekt-config.yml")
}
