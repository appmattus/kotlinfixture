/*
 * Copyright 2021-2023 Appmattus Limited
 *           2024 Detomarco
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

import org.gradle.model.internal.core.ModelNodes.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("maven-publish")
}

val kotlinVersion: String by project
val kotlinxVersion: String by project
val junitVersion: String by project
val generexVersion: String by project

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(project(":fixture"))
    api("com.github.mifmif:generex:$generexVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation(kotlin("test-junit5"))

    testImplementation(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxVersion")
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
            artifactId = "fixture-generex"
            description = "kotlinfixture module to generate random strings from regular expressions"
        }
        withType<MavenPublication> {
            pom {
                packaging = "jar"
                name = "kotlinfixture-generex"
                description = "Kotlin Fixture - Generex"
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
                    developer {
                        id = "Appmattus Limited"
                        name = "Matthew Dolan"
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
