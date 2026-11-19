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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
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
    fun `without modules the Java 8 internal packages are rejected`() {
        // As on Java 8, where there are no modules to read the exported packages from
        val withoutModules = scanJavac(jdkPackages = null)

        assertFalse("com.sun.tools.javac.code.Symbol" in withoutModules)
        assertTrue("com.sun.tools.javac.Main" in withoutModules)
    }

    @Test
    fun `with modules the non-exported packages are rejected`() {
        val withModules = scanJavac(Classes.jdkPackages)

        assertFalse("com.sun.tools.javac.code.Symbol" in withModules)
        assertTrue("com.sun.tools.javac.Main" in withModules)
    }

    @Test
    fun `Java 8 internal packages contain no exported package`() {
        val exported = assertNotNull(Classes.jdkPackages).exported

        val rejected = exported.filter { name ->
            java8InternalPackages.any { name == it || name.startsWith("$it.") }
        }

        assertEquals(emptyList(), rejected)
    }

    @Test
    fun `Java 8 internal packages that still exist are not exported`() {
        val packages = assertNotNull(Classes.jdkPackages)

        val stillExisting = java8InternalPackages.filter { it in packages.exported || it in packages.nonExported }

        assertTrue(stillExisting.isNotEmpty())
        assertTrue(stillExisting.all { it in packages.nonExported })
    }

    @Test
    fun `non-exported package with an exported subpackage is not rejected`() {
        val packages = Classes.JdkPackages(exported = setOf("a.b.c"), nonExported = setOf("a", "a.b", "a.x"))

        assertEquals(listOf("a.x"), packages.rejectable)
    }

    @Test
    fun `implementations of an abstract class are its subclasses`() {
        val implementations = assertNotNull(Classes.implementationsOf(Number::class.java)).map { it.name }

        assertTrue("java.math.BigDecimal" in implementations)
        assertTrue("java.util.concurrent.atomic.AtomicLong" in implementations)
    }

    @Test
    fun `implementations of an interface are the classes implementing it`() {
        val implementations = assertNotNull(Classes.implementationsOf(CharSequence::class.java)).map { it.name }

        assertTrue("java.lang.String" in implementations)
        assertTrue("java.lang.StringBuilder" in implementations)
    }

    @Test
    fun `implementations are cached`() {
        assertSame(Classes.implementationsOf(Number::class.java), Classes.implementationsOf(Number::class.java))
    }

    @Test
    fun `implementations are cached once across threads`() {
        // Not asked for by any other test, so the cache starts empty for it
        val executor = Executors.newFixedThreadPool(THREADS)
        try {
            val start = CountDownLatch(1)
            val results = List(THREADS) {
                executor.submit<List<*>> {
                    start.await()
                    Classes.implementationsOf(java.util.EventListener::class.java)
                }
            }
            start.countDown()

            val lists = results.map { assertNotNull(it.get()) }
            lists.forEach { assertSame(lists.first(), it) }
        } finally {
            executor.shutdown()
        }
    }

    @Test
    fun `class the scan did not find has no implementations`() {
        // sun is an internal package, so it is not scanned
        assertNull(Classes.implementationsOf(Class.forName("sun.misc.Unsafe")))
    }

    private fun scanJavac(jdkPackages: Classes.JdkPackages?): Set<String> =
        ClassGraph()
            .enableSystemJarsAndModules()
            .acceptPackages("com.sun.tools.javac")
            .rejectInternalPackages(jdkPackages)
            .enableClassInfo()
            .scan()
            .use { result -> result.allClasses.map { it.name }.toSet() }

    private companion object {
        private const val THREADS = 16
    }
}
