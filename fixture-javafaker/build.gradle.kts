/*
 * Copyright 2021 Appmattus Limited
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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.android.lint")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

apply(from = "$rootDir/gradle/scripts/jacoco.gradle.kts")

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(project(":fixture"))
    api("com.github.javafaker:javafaker:${Versions.javafaker}")
    api("org.yaml:snakeyaml:android") {
        version {
            strictly("1.27")
        }
    }

    testImplementation("junit:junit:${Versions.junit4}")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}")

    testImplementation(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")
}

lintOptions {
    isAbortOnError = true
    isWarningsAsErrors = true
    htmlOutput = file("$buildDir/reports/lint-results.html")
    xmlOutput = file("$buildDir/reports/lint-results.xml")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.named("check") {
    finalizedBy(rootProject.tasks.named("detekt"))
    finalizedBy(rootProject.tasks.named("markdownlint"))
}
