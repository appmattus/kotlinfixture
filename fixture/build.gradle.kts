/*
 * Copyright 2021-2023 Appmattus Limited
 *           2024 Detomarco Limited
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
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
}

apply(from = "$rootDir/gradle/scripts/jacoco.gradle.kts")

val classgraphVersion: String by project
val jodaTimeVersion: String by project
val threeTenVersion: String by project
val kTormVersion: String by project
val junit4Version: String by project
val mockitoKotlinVersion: String by project
val serializationVersion: String by project
val marcellogalhardoVersion: String by project
val flextradeVersion: String by project
val easyrandomVersion: String by project
val kotestVersion: String by project
val kotlinxSerializatioVersion: String by project

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.classgraph:classgraph:${classgraphVersion}")
    implementation(kotlin("reflect"))

    compileOnly("joda-time:joda-time:${jodaTimeVersion}")
    testImplementation("joda-time:joda-time:${jodaTimeVersion}")

    compileOnly("org.threeten:threetenbp:${threeTenVersion}")
    testImplementation("org.threeten:threetenbp:${threeTenVersion}")

    compileOnly("org.ktorm:ktorm-core:${kTormVersion}")
    testImplementation("org.ktorm:ktorm-core:${kTormVersion}")

    testImplementation("junit:junit:${junit4Version}")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:${mockitoKotlinVersion}")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinxSerializatioVersion}")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")

    // Used for ComparisonTest
    testImplementation("com.github.marcellogalhardo:kotlin-fixture:${marcellogalhardoVersion}")
    testImplementation("com.flextrade.jfixture:kfixture:${flextradeVersion}")
    testImplementation("org.jeasy:easy-random-core:${easyrandomVersion}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    enabled = true
    // Remove `plain` postfix from jar file name
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            from(components["java"])
            artifactId = "fixture"
            description = "Fixtures for Kotlin providing generated values for unit testing"
        }
        withType<MavenPublication> {
            pom {
                packaging = "jar"
                name = "kotlinfixture"
                description = "Kotlin Fixture"
                url = "https://github.com/detomarco/kotlinfixture"
                inceptionYear = "2024"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://spdx.org/licenses/Apache-2.0.html"
                    }
                }
                developers {
                    developer {
                        id = "detomarco"
                        name = "Marco De Toma"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/detomarco/kotlinfixture.git"
                    developerConnection = "scm:git:ssh://github.com/detomarco/kotlinfixture.git"
                    url = "http://github.com/detomarco/kotlinfixture"
                }
            }
        }
    }
    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

tasks.named("check") {
    finalizedBy(rootProject.tasks.named("detekt"))
}
