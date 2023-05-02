/*
 * Copyright 2023 Appmattus Limited
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

import com.appmattus.kotlinfixture.config.TestGenerator.fixture
import kotlinx.serialization.Serializable
import kotlin.test.Test

class SerializableTest {

    @Serializable
    private data class ErrorCodeDto(
        val errorCode: String,
        val errorDetail: String,
        val errorDescription: String
    )

    @Test
    fun `serializing and deserializing ErrorCodeDto should result in original instance`() {
        repeat(100) {
            val original = fixture<ErrorCodeDto>()
            // Serializable generates synthetic constructors with nullable parameters so we ensure we verify we don't use that constructor
            @Suppress("SENSELESS_COMPARISON")
            runCatching {
                require(original.errorCode != null)
                require(original.errorDetail != null)
                require(original.errorDescription != null)
            }.onFailure {
                println(original)
                throw IllegalArgumentException()
            }
        }
    }
}
