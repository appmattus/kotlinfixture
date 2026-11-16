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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Mutable properties can be ignored, and lateinit properties are left uninitialised when no value can be generated.
 * See https://github.com/appmattus/kotlinfixture/issues/90
 */
class IgnorePropertyTest {

    // No implementation exists, so no value can be generated
    interface Repository {
        fun find(id: Int): String
    }

    data class Post(val id: Int, val title: String) {
        lateinit var repository: Repository
        lateinit var author: String
        var notes: String = DEFAULT
    }

    class OtherPost {
        var notes: String = DEFAULT
    }

    class NonLateinitUnresolvable {
        var unresolvable: Repository? = null
    }

    private val Post.isRepositoryInitialised: Boolean
        get() = runCatching { repository }.isSuccess

    private val Post.isAuthorInitialised: Boolean
        get() = runCatching { author }.isSuccess

    @Test
    fun `lateinit property with no generated value is left uninitialised`() {
        val post = kotlinFixture()<Post>()

        assertTrue { !post.isRepositoryInitialised }
        assertFailsWith<UninitializedPropertyAccessException> { post.repository }
    }

    @Test
    fun `lateinit property with a generated value is populated`() {
        val post = kotlinFixture()<Post>()

        assertTrue { post.isAuthorInitialised }
        assertNotEquals(DEFAULT, post.notes)
    }

    @Test
    fun `non-lateinit property with no generated value still fails`() {
        val fixture = kotlinFixture {
            // Without this the property could be generated as null
            nullabilityStrategy(NeverNullStrategy)
        }

        assertFailsWith<UnsupportedOperationException> {
            fixture<NonLateinitUnresolvable>()
        }
    }

    @Test
    fun `ignoreProperty by reference keeps the initial value`() {
        val fixture = kotlinFixture {
            ignoreProperty(Post::notes)
        }

        val post = fixture<Post>()

        assertEquals(DEFAULT, post.notes)
        assertTrue { post.isAuthorInitialised }
    }

    @Test
    fun `ignoreProperty by reference leaves a lateinit property uninitialised`() {
        val fixture = kotlinFixture {
            ignoreProperty(Post::author)
        }

        val post = fixture<Post>()

        assertTrue { !post.isAuthorInitialised }
        assertNotEquals(DEFAULT, post.notes)
    }

    @Test
    fun `ignoreProperty by name keeps the initial value`() {
        val fixture = kotlinFixture {
            ignoreProperty<Post>("notes")
        }

        val post = fixture<Post>()

        assertEquals(DEFAULT, post.notes)
    }

    @Test
    fun `ignoreProperty only applies to the given class`() {
        val fixture = kotlinFixture {
            ignoreProperty(Post::notes)
        }

        assertEquals(DEFAULT, fixture<Post>().notes)
        assertNotEquals(DEFAULT, fixture<OtherPost>().notes)
    }

    @Test
    fun `ignoreProperty can be set for a single call`() {
        val fixture = kotlinFixture()

        assertEquals(DEFAULT, fixture<Post> { ignoreProperty(Post::notes) }.notes)
        assertNotEquals(DEFAULT, fixture<Post>().notes)
    }

    @Test
    fun `ignoreProperty for a Java setter does not call it`() {
        val fixture = kotlinFixture {
            ignoreProperty(FixtureTestJavaClass::setMutable)
        }

        val result = fixture<FixtureTestJavaClass>()

        assertNull(result.mutable)
        assertTrue { result.constructor.isNotEmpty() }
    }

    @Test
    fun `Java setter is called by default`() {
        val result = kotlinFixture()<FixtureTestJavaClass>()

        assertTrue { !result.mutable.isNullOrEmpty() }
    }

    @Test
    fun `ignoreLateinitProperties leaves every lateinit property uninitialised`() {
        val fixture = kotlinFixture {
            ignoreLateinitProperties()
        }

        val post = fixture<Post>()

        assertTrue { !post.isAuthorInitialised }
        assertTrue { !post.isRepositoryInitialised }
        assertNotEquals(DEFAULT, post.notes)
    }

    @Test
    fun `constructor parameters are still generated when ignored`() {
        val fixture = kotlinFixture {
            ignoreProperty<Post>("title")
        }

        val post = fixture<Post>()

        assertTrue { post.title.isNotEmpty() }
    }

    private companion object {
        private const val DEFAULT = "default"
    }
}
