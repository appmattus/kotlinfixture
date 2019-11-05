/*
 * Copyright 2019 Appmattus Limited
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

package com.appmattus.kotlinfixture.resolver

import android.net.Uri
import com.appmattus.kotlinfixture.kotlinFixture
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class AndroidUriResolverTest {
    private val fixture = kotlinFixture()

    @Test
    fun `Uri class returns uri`() {
        val result = fixture<Uri>()

        assertNotNull(result)
        assertTrue {
            Uri::class.isInstance(result)
        }
    }

    @Test
    fun `Random values returned`() {
        assertIsRandom {
            fixture<Uri>()
        }
    }

    @Test
    fun `Uses seeded random`() {
        val value1 = fixture<Uri> { random = Random(0) }
        val value2 = fixture<Uri> { random = Random(0) }

        assertEquals(value1.toString(), value2.toString())
    }

    private fun assertIsRandom(block: () -> Any?) {
        val initial = block()

        repeat(1000) {
            if (initial != block()) return
        }

        fail("Value always equal to $initial")
    }
}
