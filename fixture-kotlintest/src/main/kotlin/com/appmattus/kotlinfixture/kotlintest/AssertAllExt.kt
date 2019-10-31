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

package com.appmattus.kotlinfixture.kotlintest

import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.typeOf
import io.kotlintest.properties.Gen
import io.kotlintest.properties.PropertyContext
import io.kotlintest.properties.assertAll

@Suppress("UNCHECKED_CAST")
inline fun <reified A> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>, fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C, reified D> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    KotlinTestGenerator(this, typeOf<D>()) as Gen<D>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    KotlinTestGenerator(this, typeOf<D>()) as Gen<D>,
    KotlinTestGenerator(this, typeOf<E>()) as Gen<E>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = assertAll(
    DEFAULT_ITERATIONS,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    KotlinTestGenerator(this, typeOf<D>()) as Gen<D>,
    KotlinTestGenerator(this, typeOf<E>()) as Gen<E>,
    KotlinTestGenerator(this, typeOf<F>()) as Gen<F>,
    fn
)

const val DEFAULT_ITERATIONS = 1000
