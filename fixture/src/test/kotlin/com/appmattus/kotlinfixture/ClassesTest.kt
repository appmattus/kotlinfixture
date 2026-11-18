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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
}
