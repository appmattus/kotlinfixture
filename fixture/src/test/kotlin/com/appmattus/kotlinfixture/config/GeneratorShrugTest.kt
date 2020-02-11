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

package com.appmattus.kotlinfixture.config

import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_API_USAGE")
class GeneratorShrugTest {

    // Boolean, Double, Float, Int, Long, UInt, ULong

    data class TestClass(
        val boolean: Boolean,
        val double: Double,
        val float: Float,
        val int: Int,
        val long: Long,
        val uInt: UInt,
        val uLong: ULong
    )

    val fixture = kotlinFixture {
        factory<Boolean> { false }
        factory<Double> { 0.0 }
        factory<Float> { 0f }
        factory<Int> { 0 }
        factory<Long> { 0L }
        factory<UInt> { 0.toUInt() }
        factory<ULong> { 0.toULong() }
    }

    @Test
    fun `Default generators always return the same content`() {
        val original = fixture<TestClass>()

        repeat(100) {
            assertEquals(original.toString(), fixture<TestClass>().toString())
        }
    }

    @Test
    fun `Random values returned for Boolean shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<Boolean> { `¯＼_(ツ)_／¯`() }
            }.boolean
        }
    }

    private fun ConfigurationBuilder.seededRandom() {
        random = Random(0)
    }

    @Test
    fun `Boolean shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<Boolean> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<Boolean> { seededRandom() }, fixture<Boolean> { seededRandom() })
    }

    @Test
    fun `Random values returned for Double shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<Double> { `¯＼_(ツ)_／¯`() }
            }.double
        }
    }

    @Test
    fun `Double shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<Double> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<Double> { seededRandom() }, fixture<Double> { seededRandom() })
    }

    @Test
    fun `Random values returned for Float shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<Float> { `¯＼_(ツ)_／¯`() }
            }.float
        }
    }

    @Test
    fun `Float shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<Float> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<Float> { seededRandom() }, fixture<Float> { seededRandom() })
    }

    @Test
    fun `Random values returned for Int shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<Int> { `¯＼_(ツ)_／¯`() }
            }.int
        }
    }

    @Test
    fun `Int shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<Int> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<Int> { seededRandom() }, fixture<Int> { seededRandom() })
    }

    @Test
    fun `Random values returned for Long shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<Long> { `¯＼_(ツ)_／¯`() }
            }.long
        }
    }

    @Test
    fun `Long shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<Long> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<Long> { seededRandom() }, fixture<Long> { seededRandom() })
    }


    @Test
    fun `Random values returned for UInt shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<UInt> { `¯＼_(ツ)_／¯`() }
            }.uInt
        }
    }

    @Test
    fun `UInt shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<UInt> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<UInt> { seededRandom() }, fixture<UInt> { seededRandom() })
    }

    @Test
    fun `Random values returned for ULong shrug`() {
        assertIsRandom {
            fixture<TestClass> {
                factory<ULong> { `¯＼_(ツ)_／¯`() }
            }.uLong
        }
    }

    @Test
    fun `ULong shrug uses seeded random`() {
        val fixture = fixture.new {
            factory<ULong> { `¯＼_(ツ)_／¯`() }
        }

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals(fixture<ULong> { seededRandom() }, fixture<ULong> { seededRandom() })
    }
}
