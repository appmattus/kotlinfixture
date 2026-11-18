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

import android.annotation.SuppressLint
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import java.lang.module.ModuleFinder

internal object Classes {
    /**
     * Packages internal to the JDK and kotlin-reflect, with their subpackages. Their classes are implementation details
     * that are not meaningful values to generate, and skipping them roughly halves the time taken to scan.
     *
     * Only packages that are wholly internal are listed, as other `com.sun` and `jdk` packages include supported APIs,
     * such as `com.sun.net.httpserver` and `jdk.jfr`. On Java 9 and later, the JDK's other non-exported packages are
     * skipped too, see [JdkPackages].
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
            .rejectInternalPackages(jdkPackages)
            .enableClassInfo()
            .scan()
    }

    /**
     * The packages of the JDK's modules, or null before Java 9, and on Android, where there are no modules.
     */
    val jdkPackages: JdkPackages? by lazy {
        val hasModules = try {
            Class.forName("java.lang.module.ModuleFinder")
            true
        } catch (expected: ClassNotFoundException) {
            false
        }

        if (hasModules) readJdkPackages() else null
    }

    // Only called when java.lang.module is available
    @SuppressLint("NewApi")
    private fun readJdkPackages(): JdkPackages {
        val descriptors = ModuleFinder.ofSystem().findAll().map { it.descriptor() }
        val exported = descriptors.flatMap { descriptor ->
            descriptor.exports().filterNot { it.isQualified }.map { it.source() }
        }.toSet()

        return JdkPackages(exported = exported, nonExported = descriptors.flatMap { it.packages() }.toSet() - exported)
    }

    /**
     * Rejects [internalPackages] and the JDK's non-exported packages. ClassGraph rejects a package with its
     * subpackages, so a non-exported package is only rejected when none of its subpackages are exported.
     */
    fun ClassGraph.rejectInternalPackages(jdkPackages: JdkPackages?): ClassGraph = apply {
        internalPackages.forEach { rejectPackages(it) }
        jdkPackages?.rejectable?.forEach { rejectPackages(it) }
    }

    /**
     * The JDK only supports the packages its modules export to every module. Classes in other packages are
     * implementation details, and since Java 16 cannot be created by reflection without `--add-opens`, so they are not
     * useful implementations of abstract types.
     */
    class JdkPackages(val exported: Set<String>, val nonExported: Set<String>) {
        /** The non-exported packages that can be rejected without rejecting an exported subpackage */
        val rejectable: List<String> by lazy {
            nonExported.filter { name -> exported.none { it.startsWith("$name.") } }
        }
    }
}
