/*
 * Copyright 2024 Detomarco
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

plugins {
    kotlin("jvm")
    id("maven-publish")
}

val kotestVersion: String by project
val kotlinxVersion: String by project

dependencies {
    implementation(project(":fixture"))
    api("io.kotest:kotest-property-jvm:$kotestVersion")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxVersion")
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            from(components["java"])
            artifactId = "fixture-kotest"
            description = "kotlinfixture module for property based testing for"
        }
        withType<MavenPublication> {
            pom {
                packaging = "jar"
                name = "kotlinfixture-kotest"
                description = "Kotlin Fixture - Kotest"
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
                    url = "https://github.com/detomarco/kotlinfixture"
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
