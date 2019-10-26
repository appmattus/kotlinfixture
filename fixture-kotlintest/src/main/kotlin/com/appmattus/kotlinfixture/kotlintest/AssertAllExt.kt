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
    1000,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>, fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B) -> Unit
) = assertAll(
    1000,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C) -> Unit
) = assertAll(
    1000,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    fn
)

@Suppress("UNCHECKED_CAST")
inline fun <reified A, reified B, reified C, reified D> Fixture.assertAll(
    noinline fn: PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = assertAll(
    1000,
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
    1000,
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
    1000,
    KotlinTestGenerator(this, typeOf<A>()) as Gen<A>,
    KotlinTestGenerator(this, typeOf<B>()) as Gen<B>,
    KotlinTestGenerator(this, typeOf<C>()) as Gen<C>,
    KotlinTestGenerator(this, typeOf<D>()) as Gen<D>,
    KotlinTestGenerator(this, typeOf<E>()) as Gen<E>,
    KotlinTestGenerator(this, typeOf<F>()) as Gen<F>,
    fn
)
