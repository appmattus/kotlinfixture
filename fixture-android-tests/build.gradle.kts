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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
}

apply(from = "$rootDir/gradle/scripts/jacoco-android.gradle.kts")

android {
    namespace = "com.appmattus.fixture.android.tests"

    compileSdk = 37

    defaultConfig {
        minSdk = 21
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":fixture"))

    testImplementation("androidx.test:core:${Versions.AndroidX.testCore}")
    testImplementation("androidx.test:runner:${Versions.AndroidX.testRunner}")
    testImplementation("androidx.test.ext:junit:${Versions.AndroidX.testExtJunit}")
    testImplementation("org.robolectric:robolectric:${Versions.robolectric}") {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

    testImplementation("junit:junit:${Versions.junit4}")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}")

    testImplementation(kotlin("reflect"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

tasks.named("check") {
    finalizedBy(rootProject.tasks.named("detekt"))
    finalizedBy(rootProject.tasks.named("markdownlint"))
}
