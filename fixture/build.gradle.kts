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
    id("org.jetbrains.kotlin.jvm")
    id("com.android.lint")
}

apply(from = "$rootDir/bintray.gradle.kts")
apply(from = "$rootDir/codecoverage.gradle.kts")

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.classgraph:classgraph:4.8.52")
    implementation(kotlin("reflect"))

    compileOnly("joda-time:joda-time:2.10.5")
    testImplementation("joda-time:joda-time:2.10.5")

    compileOnly("org.threeten:threetenbp:1.4.0")
    testImplementation("org.threeten:threetenbp:1.4.0")

    compileOnly(files("${System.getenv("ANDROID_HOME")}/platforms/android-29/android.jar"))

    testImplementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    // Used for ComparisonTest
    @Suppress("GradleDependency")
    testImplementation("com.github.marcellogalhardo:kotlin-fixture:0.0.2")
    testImplementation("com.flextrade.jfixture:kfixture:0.2.0")
    testImplementation("org.jeasy:easy-random-core:4.0.0")
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
