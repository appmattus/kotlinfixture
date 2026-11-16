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

import kotlin.properties.ObservableProperty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

/**
 * Abstract classes and interfaces are only resolved to implementations that can be used: visible to KotlinFixture's
 * class loader, not internal to the JDK or kotlin-reflect, and loadable. See
 * https://github.com/appmattus/kotlinfixture/issues/111
 */
class AbstractClassImplementationTest {

    interface Shape

    class WorkingShape : Shape

    // Initialising this class always fails, as when one of a class's dependencies is missing at runtime
    class BrokenShape : Shape {
        companion object {
            @Suppress("unused")
            private val broken: Int = error("Unable to initialise")
        }
    }

    private val fixtureClassLoader = Fixture::class.java.classLoader

    @Test
    fun `implementation that cannot be loaded is skipped`() {
        val fixture = kotlinFixture()

        repeat(SAMPLES) {
            assertEquals(WorkingShape::class, fixture<Shape>()::class)
        }
    }

    @Test
    fun `abstract type with only internal implementations gives a normal error`() {
        // Its only implementation is internal to kotlin-reflect and cannot be reflected on
        assertFailsWith<UnsupportedOperationException> {
            kotlinFixture()<ObservableProperty<String>>()
        }
    }

    @Test
    fun `implementations are visible to KotlinFixture's class loader`() {
        val fixture = kotlinFixture()

        // Number has implementations in the build tool's own class loader when tests run under Gradle
        val classes = List(SAMPLES) { fixture<Number>() }.map { it::class.java }.toSet()

        classes.forEach { clazz ->
            assertSame(clazz, Class.forName(clazz.name, false, fixtureClassLoader), "$clazz is not visible")
        }
    }

    private companion object {
        private const val SAMPLES = 200
    }
}
