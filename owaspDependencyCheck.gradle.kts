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

import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.owasp:dependency-check-gradle:${Versions.dependencyCheckGradlePlugin}")
    }
}

subprojects {
    apply<DependencyCheckPlugin>()

    configure<DependencyCheckExtension> {
        failBuildOnCVSS = 0f

        suppressionFile = file("$rootDir/cve-suppressions.xml").toString()

        analyzers.assemblyEnabled = false

        skipConfigurations = listOf(
            "lintClassPath",
            "jacocoAgent",
            "jacocoAnt",
            "kotlinCompilerClasspath",
            "kotlinCompilerPluginClasspath"
        )
    }
}
