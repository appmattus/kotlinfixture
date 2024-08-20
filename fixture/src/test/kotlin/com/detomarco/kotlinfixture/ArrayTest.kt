/*
 * Copyright 2024 Appmattus Limited
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

package com.detomarco.kotlinfixture

import com.detomarco.kotlinfixture.config.TestGenerator.fixture
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * More robust testing around Array fixtures as we now have special handling for arrays of primitive types
 * See https://youtrack.jetbrains.com/issue/KT-52170/Reflection-typeOfArrayLong-gives-classifier-LongArray
 */
@OptIn(ExperimentalUnsignedTypes::class)
class ArrayTest {

    @Test
    fun arrayBoolean() {
        assertIsRandom {
            fixture<Array<Boolean>>().onEach {
                assertTrue { Boolean::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayByte() {
        assertIsRandom {
            fixture<Array<Byte>>().onEach {
                assertTrue { Byte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayDouble() {
        assertIsRandom {
            fixture<Array<Double>>().onEach {
                assertTrue { Double::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayFloat() {
        assertIsRandom {
            fixture<Array<Float>>().onEach {
                assertTrue { Float::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayInt() {
        assertIsRandom {
            fixture<Array<Int>>().onEach {
                assertTrue { Int::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayLong() {
        assertIsRandom {
            fixture<Array<Long>>().onEach {
                assertTrue { Long::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayShort() {
        assertIsRandom {
            fixture<Array<Short>>().onEach {
                assertTrue { Short::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayChar() {
        assertIsRandom {
            fixture<Array<Char>>().onEach {
                assertTrue { Char::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUByte() {
        assertIsRandom {
            fixture<Array<UByte>>().onEach {
                assertTrue { UByte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUInt() {
        assertIsRandom {
            fixture<Array<UInt>>().onEach {
                assertTrue { UInt::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayULong() {
        assertIsRandom {
            fixture<Array<ULong>>().onEach {
                assertTrue { ULong::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUShort() {
        assertIsRandom {
            fixture<Array<UShort>>().onEach {
                assertTrue { UShort::class.isInstance(it) }
            }
        }
    }

    @Test
    fun booleanArray() {
        assertIsRandom {
            fixture<BooleanArray>().onEach {
                assertTrue { Boolean::class.isInstance(it) }
            }
        }
    }

    @Test
    fun byteArray() {
        assertIsRandom {
            fixture<ByteArray>().onEach {
                assertTrue { Byte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun doubleArray() {
        assertIsRandom {
            fixture<DoubleArray>().onEach {
                assertTrue { Double::class.isInstance(it) }
            }
        }
    }

    @Test
    fun floatArray() {
        assertIsRandom {
            fixture<FloatArray>().onEach {
                assertTrue { Float::class.isInstance(it) }
            }
        }
    }

    @Test
    fun intArray() {
        assertIsRandom {
            fixture<IntArray>().onEach {
                assertTrue { Int::class.isInstance(it) }
            }
        }
    }

    @Test
    fun longArray() {
        assertIsRandom {
            fixture<LongArray>().onEach {
                assertTrue { Long::class.isInstance(it) }
            }
        }
    }

    @Test
    fun shortArray() {
        assertIsRandom {
            fixture<ShortArray>().onEach {
                assertTrue { Short::class.isInstance(it) }
            }
        }
    }

    @Test
    fun charArray() {
        assertIsRandom {
            fixture<CharArray>().onEach {
                assertTrue { Char::class.isInstance(it) }
            }
        }
    }

    @Test
    fun uByteArray() {
        assertIsRandom {
            fixture<UByteArray>().onEach {
                assertTrue { UByte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun uIntArray() {
        assertIsRandom {
            fixture<UIntArray>().onEach {
                assertTrue { UInt::class.isInstance(it) }
            }
        }
    }

    @Test
    fun uLongArray() {
        assertIsRandom {
            fixture<ULongArray>().onEach {
                assertTrue { ULong::class.isInstance(it) }
            }
        }
    }

    @Test
    fun uShortArray() {
        assertIsRandom {
            fixture<UShortArray>().onEach {
                assertTrue { UShort::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayBooleanNullable() {
        assertIsRandom {
            fixture<Array<Boolean?>>().onEach {
                assertTrue { it == null || Boolean::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayByteNullable() {
        assertIsRandom {
            fixture<Array<Byte?>>().onEach {
                assertTrue { it == null || Byte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayDoubleNullable() {
        assertIsRandom {
            fixture<Array<Double?>>().onEach {
                assertTrue { it == null || Double::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayFloatNullable() {
        assertIsRandom {
            fixture<Array<Float?>>().onEach {
                assertTrue { it == null || Float::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayIntNullable() {
        assertIsRandom {
            fixture<Array<Int?>>().onEach {
                assertTrue { it == null || Int::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayLongNullable() {
        assertIsRandom {
            fixture<Array<Long?>>().onEach {
                assertTrue { it == null || Long::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayShortNullable() {
        assertIsRandom {
            fixture<Array<Short?>>().onEach {
                assertTrue { it == null || Short::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayCharNullable() {
        assertIsRandom {
            fixture<Array<Char?>>().onEach {
                assertTrue { it == null || Char::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUByteNullable() {
        assertIsRandom {
            fixture<Array<UByte?>>().onEach {
                assertTrue { it == null || UByte::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUIntNullable() {
        assertIsRandom {
            fixture<Array<UInt?>>().onEach {
                assertTrue { it == null || UInt::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayULongNullable() {
        assertIsRandom {
            fixture<Array<ULong?>>().onEach {
                assertTrue { it == null || ULong::class.isInstance(it) }
            }
        }
    }

    @Test
    fun arrayUShortNullable() {
        assertIsRandom {
            fixture<Array<UShort?>>().onEach {
                assertTrue { it == null || UShort::class.isInstance(it) }
            }
        }
    }
}
