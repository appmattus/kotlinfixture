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

import com.novoda.gradle.release.PublishExtension
import com.novoda.gradle.release.ReleasePlugin

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.novoda:bintray-release:0.9.1")
    }
}

apply<ReleasePlugin>()

configure<PublishExtension> {
    bintrayUser = System.getenv("BINTRAY_USER") ?: System.getProperty("BINTRAY_USER") ?: "unknown"
    bintrayKey = System.getenv("BINTRAY_KEY") ?: System.getProperty("BINTRAY_KEY") ?: "unknown"

    groupId = "com.appmattus.fixture"
    artifactId = project.name
    publishVersion = System.getenv("CIRCLE_TAG") ?: System.getProperty("CIRCLE_TAG")
            ?: System.getenv("TRAVIS_TAG") ?: System.getProperty("TRAVIS_TAG")
            ?: "unknown"

    repoName = "maven"
    userOrg = "appmattus"
    desc = "Fixtures for Kotlin providing generated values for unit testing"
    website = "https://github.com/appmattus/kotlinfixture"
    setLicences("Apache-2.0")

    dryRun = false
}
