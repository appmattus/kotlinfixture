/*
 * Copyright 2020 Appmattus Limited
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

package io.github.detomarco.kotlinfixture

import io.github.detomarco.kotlinfixture.decorator.nullability.NeverNullStrategy
import io.github.detomarco.kotlinfixture.decorator.nullability.nullabilityStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FixtureRepeatCountTest {

    data class RepeatCountClass(val readOnly: List<Int>, private var private: List<Int>) {
        var member: List<Int>? = null
        val alsoReadOnly: List<Int>? = null
        fun getPrivate(): List<Int> = private
    }

    @Test
    fun `repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount { 1 }
        }

        val list = fixture<List<String>>()
        assertEquals(1, list.size)
    }

    @Test
    fun `repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val list = fixture<List<String>> {
            repeatCount { 2 }
        }
        assertEquals(2, list.size)
    }

    @Test
    fun `repeatCount can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount { 1 }
        }

        val list = fixture<List<String>> {
            repeatCount { 2 }
        }
        assertEquals(2, list.size)
    }

    @Test
    fun `repeatCount can be overridden for constructor property`() {
        val fixture = kotlinFixture {
            repeatCount { 2 }
            repeatCount(RepeatCountClass::readOnly) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount(RepeatCountClass::readOnly) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val item = fixture<RepeatCountClass> {
            repeatCount(RepeatCountClass::readOnly) { 2 }
        }
        assertEquals(2, item.readOnly.size)
    }

    @Test
    fun `constructor property can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount(RepeatCountClass::readOnly) { 1 }
        }

        val item = fixture<RepeatCountClass> {
            repeatCount(RepeatCountClass::readOnly) { 2 }
        }
        assertEquals(2, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            repeatCount(RepeatCountClass::readOnly) { 1 }
        }
        val fixture = baseFixture.new {
            repeatCount(RepeatCountClass::readOnly) { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.readOnly.size)
    }

    @Test
    fun `repeatCount can be overridden for private constructor property`() {
        val fixture = kotlinFixture {
            repeatCount { 2 }
            repeatCount<RepeatCountClass>("private") { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount<RepeatCountClass>("private") { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val item = fixture<RepeatCountClass> {
            repeatCount<RepeatCountClass>("private") { 2 }
        }
        assertEquals(2, item.getPrivate().size)
    }

    @Test
    fun `private constructor property overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount<RepeatCountClass>("private") { 1 }
        }

        val item = fixture<RepeatCountClass> {
            repeatCount<RepeatCountClass>("private") { 2 }
        }
        assertEquals(2, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be overridden in new when already overridden`() {
        val baseFixture = kotlinFixture {
            repeatCount<RepeatCountClass>("private") { 1 }
        }
        val fixture = baseFixture.new {
            repeatCount<RepeatCountClass>("private") { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.getPrivate().size)
    }

    @Test
    fun `repeatCount can be overridden for member property`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            repeatCount { 2 }
            repeatCount(RepeatCountClass::member) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            repeatCount(RepeatCountClass::member) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
        }

        val item = fixture<RepeatCountClass> {
            repeatCount(RepeatCountClass::member) { 2 }
        }
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `member property repeatCount overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            repeatCount(RepeatCountClass::member) { 1 }
        }

        val item = fixture<RepeatCountClass> {
            repeatCount(RepeatCountClass::member) { 2 }
        }
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            repeatCount(RepeatCountClass::member) { 1 }
        }
        val fixture = baseFixture.new {
            repeatCount(RepeatCountClass::member) { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `read only property repeatCount cannot be set in fixture initialisation`() {
        assertThrows<IllegalStateException> {
            kotlinFixture {
                repeatCount(RepeatCountClass::alsoReadOnly) { 1 }
            }
        }
    }

    @Test
    fun `read only property repeatCount cannot be overridden in fixture creation`() {
        assertThrows<IllegalStateException> {
            val fixture = kotlinFixture()

            fixture<RepeatCountClass> {
                repeatCount(RepeatCountClass::alsoReadOnly) { 1 }
            }
        }
    }

    @Test
    fun `repeatCount can be overridden for java constructor property`() {
        val fixture = kotlinFixture {
            repeatCount { 2 }
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(1, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(1, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val instance = fixture<FixtureRepeatCountTestJavaClass> {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 2 }
        }
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass> {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 2 }
        }
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 1 }
        }
        val fixture = baseFixture.new {
            repeatCount<FixtureRepeatCountTestJavaClass>("arg0") { 2 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `repeatCount can be overridden for java member property`() {
        val fixture = kotlinFixture {
            repeatCount { 2 }
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(1, instance.mutable.size)
    }

    @Test
    fun `java member property can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(1, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val instance = fixture<FixtureRepeatCountTestJavaClass> {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 2 }
        }
        assertEquals(2, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 1 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass> {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 2 }
        }
        assertEquals(2, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 1 }
        }
        val fixture = baseFixture.new {
            repeatCount(FixtureRepeatCountTestJavaClass::setMutable) { 2 }
        }

        val instance = fixture<FixtureRepeatCountTestJavaClass>()
        assertEquals(2, instance.mutable.size)
    }
}
