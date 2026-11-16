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

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Properties with protected or private setters keep their initial value by default, but can be customised with
 * property. See https://github.com/appmattus/kotlinfixture/issues/94
 */
class RestrictedSetterTest {

    open class ProtectedSetter {
        var name: String = INITIAL
            protected set
    }

    class PrivateSetter {
        var name: String = INITIAL
            private set
    }

    class InternalSetter {
        var name: String = INITIAL
            internal set
    }

    open class ProtectedListSetter {
        var items: List<String> = emptyList()
            protected set
    }

    // No implementation exists, so no value can be generated
    interface Repository

    open class UnresolvableProtectedSetter {
        var repository: Repository? = null
            protected set
    }

    class ReadOnly {
        val name: String = INITIAL
    }

    @Test
    fun `protected setter keeps its initial value by default`() {
        assertEquals(INITIAL, kotlinFixture()<ProtectedSetter>().name)
    }

    @Test
    fun `private setter keeps its initial value by default`() {
        assertEquals(INITIAL, kotlinFixture()<PrivateSetter>().name)
    }

    @Test
    fun `internal setter is populated by default`() {
        assertNotEquals(INITIAL, kotlinFixture()<InternalSetter>().name)
    }

    @Test
    fun `protected setter is not resolved by default`() {
        // Never null forces a Repository to be generated if the setter is considered. As no value can be generated,
        // the instance would fail, even though the setter could not be called anyway
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
        }

        assertEquals(null, fixture<UnresolvableProtectedSetter>().repository)
    }

    @Test
    fun `protected setter can be overridden by reference`() {
        val fixture = kotlinFixture {
            property(ProtectedSetter::name) { "test" }
        }

        assertEquals("test", fixture<ProtectedSetter>().name)
    }

    @Test
    fun `protected setter can be overridden by name`() {
        val fixture = kotlinFixture {
            property<ProtectedSetter, String>("name") { "test" }
        }

        assertEquals("test", fixture<ProtectedSetter>().name)
    }

    @Test
    fun `private setter can be overridden by name`() {
        val fixture = kotlinFixture {
            property<PrivateSetter, String>("name") { "test" }
        }

        assertEquals("test", fixture<PrivateSetter>().name)
    }

    @Test
    fun `protected setter repeatCount can be overridden by reference`() {
        val fixture = kotlinFixture {
            repeatCount(ProtectedListSetter::items) { 3 }
            property(ProtectedListSetter::items) { listOf("a", "b", "c") }
        }

        assertEquals(3, fixture<ProtectedListSetter>().items.size)
    }

    @Test
    fun `overriding a read-only property that is not in the constructor still fails`() {
        assertFailsWith<IllegalStateException> {
            kotlinFixture {
                property(ReadOnly::name) { "test" }
            }
        }
    }

    @Test
    fun `Java restricted setters keep their initial value by default`() {
        val result = kotlinFixture()<RestrictedSetterJavaClass>()

        assertEquals(INITIAL, result.privateValue)
        assertEquals(INITIAL, result.protectedValue)
    }

    @Test
    fun `Java restricted setters can be overridden by name`() {
        val fixture = kotlinFixture {
            property<RestrictedSetterJavaClass, String>("setPrivateValue") { "private" }
            property<RestrictedSetterJavaClass, String>("setProtectedValue") { "protected" }
        }

        val result = fixture<RestrictedSetterJavaClass>()

        assertEquals("private", result.privateValue)
        assertEquals("protected", result.protectedValue)
    }

    @Test
    fun `ignoreProperty takes precedence over an override`() {
        val fixture = kotlinFixture {
            property(ProtectedSetter::name) { "test" }
            ignoreProperty<ProtectedSetter>("name")
        }

        assertEquals(INITIAL, fixture<ProtectedSetter>().name)
    }

    @Test
    fun `issue 94 example`() {
        val fixture = kotlinFixture {
            property(ProtectedSetter::name) { "test" }
        }

        repeat(10) {
            assertTrue { fixture<ProtectedSetter>().name == "test" }
        }
    }

    private companion object {
        private const val INITIAL = "initial value"
    }
}
