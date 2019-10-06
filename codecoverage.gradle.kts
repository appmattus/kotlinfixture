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

import org.kt3k.gradle.plugin.CoverallsPlugin
import org.kt3k.gradle.plugin.CoverallsPluginExtension

buildscript {
    repositories {
        maven { setUrl("https://plugins.gradle.org/m2/") }
        jcenter()
    }
    dependencies {
        classpath("gradle.plugin.com.github.kt3k.coveralls:coveralls-gradle-plugin:2.8.4")
    }
}

apply<JacocoPlugin>()
apply<CoverallsPlugin>()

tasks.getByName("test").finalizedBy(tasks.getByName("jacocoTestReport"))

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

configure<CoverallsPluginExtension> {
    sourceDirs = the<SourceSetContainer>()["main"].allSource.srcDirs.map { it.path }
    jacocoReportPath = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
}

tasks.getByName("jacocoTestReport").finalizedBy(tasks.getByName("coveralls"))

tasks.getByName("coveralls").onlyIf { System.getenv("CI")?.isNotEmpty() == true }
