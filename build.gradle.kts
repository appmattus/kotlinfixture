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
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.10.0"
    id("com.appmattus.markdown") version "0.6.0"
    id("org.jetbrains.dokka") version "0.10.1"
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

apply(from = "$rootDir/gradle/scripts/dependencyUpdates.gradle.kts")
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

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.10.0")
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

val dokka = tasks.named<DokkaTask>("dokka") {
    outputFormat = "html"
    outputDirectory = "$buildDir/reports/dokka"

    subProjects = listOf(
        "fixture",
        "fixture-generex",
        "fixture-javafaker",
        "fixture-kotest"
    )

    configuration {
        skipDeprecated = true

        sourceLink {
            path = "$rootDir"
            url = "https://github.com/appmattus/kotlinfixture/blob/master/"
            lineSuffix = "#L"
        }
    }
}

tasks.register("check").dependsOn(jacocoTestReport).dependsOn(dokka)

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
