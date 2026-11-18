/*
 * Copyright 2026 Appmattus Limited
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

import com.appmattus.kotlinfixture.Classes.rejectInternalPackages
import io.github.classgraph.ClassGraph
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClassesTest {

    @Test
    fun `classes in internal packages are not scanned`() {
        val internalClasses = Classes.classGraph.allClasses.map { it.name }.filter { name ->
            Classes.internalPackages.any { name.startsWith("$it.") }
        }

        assertEquals(emptyList(), internalClasses)
    }

    @Test
    fun `supported APIs in com sun and jdk packages are still scanned`() {
        listOf(
            "com.sun.net.httpserver.HttpServer",
            "com.sun.management.OperatingSystemMXBean",
            "jdk.jfr.Event",
            "kotlin.reflect.full.KClasses"
        ).forEach { name ->
            assertNotNull(Classes.classGraph.getClassInfo(name), "$name is not scanned")
        }
    }

    @Test
    fun `public JDK implementations are still scanned`() {
        val numbers = Classes.classGraph.getClassInfo("java.lang.Number").subclasses.map { it.name }

        assertEquals(true, "java.math.BigDecimal" in numbers && "java.util.concurrent.atomic.AtomicLong" in numbers)
    }

    @Test
    fun `non-exported JDK packages are read from the JDK's modules`() {
        val packages = assertNotNull(Classes.jdkPackages).nonExported

        assertTrue("com.sun.tools.javac.code" in packages)
        assertTrue("jdk.internal.misc" in packages)
        assertFalse("java.lang" in packages)
        assertFalse("com.sun.net.httpserver" in packages)
    }

    @Test
    fun `classes in non-exported JDK packages are not scanned`() {
        val packages = assertNotNull(Classes.jdkPackages).rejectable

        val scanned = Classes.classGraph.allClasses.map { it.name }.filter { it.substringBeforeLast('.') in packages }

        assertEquals(emptyList(), scanned)
    }

    @Test
    fun `exported package is scanned when its subpackages are not exported`() {
        assertNotNull(Classes.classGraph.getClassInfo("com.sun.tools.javac.Main"))
        assertNull(Classes.classGraph.getClassInfo("com.sun.tools.javac.code.Symbol"))
    }

    @Test
    fun `without modules only the internal packages are rejected`() {
        // As on Java 8, where there are no modules to read the exported packages from
        val withoutModules = scanJavac(jdkPackages = null)
        val withModules = scanJavac(Classes.jdkPackages)

        assertTrue("com.sun.tools.javac.code.Symbol" in withoutModules)
        assertFalse("com.sun.tools.javac.code.Symbol" in withModules)
        assertTrue("com.sun.tools.javac.Main" in withModules)
    }

    @Test
    fun `non-exported package with an exported subpackage is not rejected`() {
        val packages = Classes.JdkPackages(exported = setOf("a.b.c"), nonExported = setOf("a", "a.b", "a.x"))

        assertEquals(listOf("a.x"), packages.rejectable)
    }

    private fun scanJavac(jdkPackages: Classes.JdkPackages?): Set<String> =
        ClassGraph()
            .enableSystemJarsAndModules()
            .acceptPackages("com.sun.tools.javac")
            .rejectInternalPackages(jdkPackages)
            .enableClassInfo()
            .scan()
            .use { result -> result.allClasses.map { it.name }.toSet() }
}
