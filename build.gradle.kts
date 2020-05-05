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
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version "0.28.0"
    id("io.gitlab.arturbosch.detekt") version "1.8.0"
    id("com.appmattus.markdown") version "0.6.0"
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("com.android.tools.build:gradle:3.6.3")
    }
}

apply(from = "$rootDir/owaspDependencyCheck.gradle.kts")
apply<JacocoPlugin>()

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
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.8.0")
}

detekt {
    input = files("$projectDir")

    buildUponDefaultConfig = true

    autoCorrect = true

    config = files("detekt-config.yml")
}

val jacocoTestReport by tasks.registering(JacocoReport::class) {
    group = "Coverage reports"
    description = "Generates an aggregate report from all subprojects"

    reports {
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = false
    }

    setOnlyIf {
        true
    }
}

tasks.register("check").dependsOn(jacocoTestReport)

subprojects {
    plugins.withType<JacocoPlugin> {
        // this is executed for each project that has Jacoco plugin applied
        the<JacocoPluginExtension>().toolVersion = "0.8.5"

        tasks.withType<JacocoReport> {
            val task = this
            rootProject.tasks.getByName("jacocoTestReport").dependsOn(task)

            jacocoTestReport {
                executionData.from(task.executionData)
                additionalSourceDirs.from(task.sourceDirectories)
                additionalClassDirs.from(task.classDirectories)
            }
        }

        tasks.withType<Test> {
            rootProject.tasks.getByName("jacocoTestReport").dependsOn(this)
        }
    }
}
