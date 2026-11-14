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

import org.gradle.accessors.dm.LibrariesForLibs
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

val Project.libs: LibrariesForLibs
    get() = this.extensions.getByType()

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.buildscript.dependencyCheck)
    }
}

subprojects {
    apply<DependencyCheckPlugin>()

    configure<DependencyCheckExtension> {
        failBuildOnCVSS.set(0f)

        suppressionFile.set(file("$rootDir/cve-suppressions.xml").toString())

        analyzers.assemblyEnabled.set(false)

        skipConfigurations.set(
            listOf(
                "lintClassPath",
                "jacocoAgent",
                "jacocoAnt",
                "kotlinCompilerClasspath",
                "kotlinCompilerPluginClasspath"
            )
        )
    }
}
