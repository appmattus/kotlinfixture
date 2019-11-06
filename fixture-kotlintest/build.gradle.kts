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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.android.lint")
}

apply(from = "$rootDir/bintray.gradle.kts")
apply(from = "$rootDir/codecoverage.gradle.kts")

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":fixture"))
    implementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")

    testImplementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    testImplementation(kotlin("reflect"))
}

lintOptions {
    isAbortOnError = true
    isWarningsAsErrors = true
    htmlOutput = file("${buildDir}/reports/lint-results.html")
    xmlOutput = file("${buildDir}/reports/lint-results.xml")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Fix lack of source code when publishing pure Kotlin projects
// See https://github.com/novoda/bintray-release/issues/262
tasks.whenTaskAdded {
    if (name == "generateSourcesJarForMavenPublication") {
        this as Jar
        from(sourceSets.main.get().allSource)
    }
}

tasks.getByName("check").finalizedBy(rootProject.tasks.getByName("detekt"))
tasks.getByName("check").finalizedBy(rootProject.tasks.getByName("markdownlint"))
