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

package com.appmattus.kotlinfixture

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

internal object Classes {
    /**
     * Packages internal to the JDK and kotlin-reflect, with their subpackages. Their classes are implementation details
     * that are not meaningful values to generate, and skipping them roughly halves the time taken to scan.
     *
     * Only packages that are wholly internal are listed, as other `com.sun` and `jdk` packages include supported APIs,
     * such as `com.sun.net.httpserver` and `jdk.jfr`.
     */
    val internalPackages = listOf(
        "sun",
        "jdk.internal",
        // The JDK's internal copy of Apache Xerces and Xalan
        "com.sun.org.apache",
        "kotlin.reflect.jvm.internal"
    )

    /**
     * Only KotlinFixture's own class loader is scanned. By default ClassGraph also scans other class loaders, such as
     * the build tool's when tests run under Gradle, whose classes the code under test cannot use.
     */
    val classGraph: ScanResult by lazy {
        ClassGraph()
            .overrideClassLoaders(Classes::class.java.classLoader)
            .enableSystemJarsAndModules()
            .apply { internalPackages.forEach { rejectPackages(it) } }
            .enableClassInfo()
            .scan()
    }
}
