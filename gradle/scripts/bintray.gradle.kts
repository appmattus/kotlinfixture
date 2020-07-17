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

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    }
}

repositories {
    google()
    jcenter()
}

apply<BintrayPlugin>()
apply<MavenPublishPlugin>()

if (project.plugins.hasPlugin("java")) {
    configure<JavaPluginExtension> {
        withSourcesJar()
    }
}

val publishVersion =
    System.getenv("CIRCLE_TAG") ?: System.getProperty("CIRCLE_TAG") ?: System.getenv("TRAVIS_TAG")
    ?: System.getProperty("TRAVIS_TAG") ?: "unknown"

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("release") {
                if (project.plugins.hasPlugin("com.android.library")) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }

                groupId = "com.appmattus.fixture"
                artifactId = project.name
                version = publishVersion

                afterEvaluate {
                    artifact(project.tasks.named("dokkaJar").get())
                    if (project.plugins.hasPlugin("com.android.library")) {
                        artifact(project.tasks.named("sourcesJar").get())
                    }
                }

                pom {
                    groupId = "com.appmattus.fixture"
                    artifactId = project.name
                    version = publishVersion

                    name.set(project.name)
                    url.set("https://github.com/appmattus/kotlinfixture")
                }
            }
        }
    }
}

configure<BintrayExtension> {
    user = System.getenv("BINTRAY_USER") ?: System.getProperty("BINTRAY_USER") ?: "unknown"
    key = System.getenv("BINTRAY_KEY") ?: System.getProperty("BINTRAY_KEY") ?: "unknown"
    publish = true
    dryRun = false
    override = true

    setPublications("release")

    pkg.apply {
        repo = "maven"
        userOrg = "appmattus"
        name = project.name
        desc = "Fixtures for Kotlin providing generated values for unit testing"
        websiteUrl = "https://github.com/appmattus/kotlinfixture"
        issueTrackerUrl = "https://github.com/appmattus/kotlinfixture/issues"
        vcsUrl = "https://github.com/appmattus/kotlinfixture"
        githubRepo = "appmattus/kotlinfixture"

        setLicenses("Apache-2.0")

        version.apply {
            name = publishVersion
            vcsTag = publishVersion
        }
    }
}
