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

package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.decorator.logging.SysOutLoggingStrategy
import com.appmattus.kotlinfixture.decorator.logging.loggingStrategy
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.NeverOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FixturePropertiesRepeatCountTest {

    data class RepeatCountClass(val readOnly: List<Int>, private var private: List<Int>) {
        var member: List<Int>? = null
        val alsoReadOnly: List<Int>? = null
        fun getPrivate(): List<Int> = private
    }

    @Test
    fun `constructor property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount(RepeatCountClass::readOnly) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount(RepeatCountClass::readOnly) { 2 }
        }
        assertEquals(2, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount(RepeatCountClass::readOnly) { 1 }
        }

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount(RepeatCountClass::readOnly) { 2 }
        }
        assertEquals(2, item.readOnly.size)
    }

    @Test
    fun `constructor property repeatCount can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            propertyRepeatCount(RepeatCountClass::readOnly) { 1 }
        }
        val fixture = baseFixture.new {
            propertyRepeatCount(RepeatCountClass::readOnly) { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.readOnly.size)
    }


    @Test
    fun `private constructor property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount<RepeatCountClass>("private") { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount<RepeatCountClass>("private") { 2 }
        }
        assertEquals(2, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount<RepeatCountClass>("private") { 1 }
        }

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount<RepeatCountClass>("private") { 2 }
        }
        assertEquals(2, item.getPrivate().size)
    }

    @Test
    fun `private constructor property repeatCount can be overridden in new when already overridden`() {
        val baseFixture = kotlinFixture {
            propertyRepeatCount<RepeatCountClass>("private") { 1 }
        }
        val fixture = baseFixture.new {
            propertyRepeatCount<RepeatCountClass>("private") { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.getPrivate().size)
    }


    @Test
    fun `member property repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            propertyRepeatCount(RepeatCountClass::member) { 1 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(1, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            loggingStrategy(SysOutLoggingStrategy)
        }

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount(RepeatCountClass::member) { 2 }
        }
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            propertyRepeatCount(RepeatCountClass::member) { 1 }
        }

        val item = fixture<RepeatCountClass> {
            propertyRepeatCount(RepeatCountClass::member) { 2 }
        }
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `member property repeatCount can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
            propertyRepeatCount(RepeatCountClass::member) { 1 }
        }
        val fixture = baseFixture.new {
            propertyRepeatCount(RepeatCountClass::member) { 2 }
        }

        val item = fixture<RepeatCountClass>()
        assertEquals(2, item.member?.size)
    }

    @Test
    fun `read only property repeatCount cannot be set in fixture initialisation`() {
        assertFailsWith<IllegalStateException> {
            kotlinFixture {
                propertyRepeatCount(RepeatCountClass::alsoReadOnly) { 1 }
            }
        }
    }

    @Test
    fun `read only property repeatCount cannot be overridden in fixture creation`() {
        assertFailsWith<IllegalStateException> {
            val fixture = kotlinFixture()

            fixture<RepeatCountClass> {
                propertyRepeatCount(RepeatCountClass::alsoReadOnly) { 1 }
            }
        }
    }


    @Test
    fun `java constructor property can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 1 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass>()
        assertEquals(1, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass> {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 2 }
        }
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 1 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass> {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 2 }
        }
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `java constructor property can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 1 }
        }
        val fixture = baseFixture.new {
            propertyRepeatCount<FixturePropertiesRepeatCountTestJavaClass>("arg0") { 2 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass>()
        assertEquals(2, instance.constructor.size)
    }

    @Test
    fun `java member property can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 1 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass>()
        assertEquals(1, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass> {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 2 }
        }
        assertEquals(2, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 1 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass> {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 2 }
        }
        assertEquals(2, instance.mutable.size)
    }

    @Test
    fun `java member property can be overridden in new when already overridden in initialisation`() {
        val baseFixture = kotlinFixture {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 1 }
        }
        val fixture = baseFixture.new {
            propertyRepeatCount(FixturePropertiesRepeatCountTestJavaClass::setMutable) { 2 }
        }

        val instance = fixture<FixturePropertiesRepeatCountTestJavaClass>()
        assertEquals(2, instance.mutable.size)
    }
}
