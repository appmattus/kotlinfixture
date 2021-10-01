/*
 * Copyright 2021 Appmattus Limited
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

@file:Suppress("TooManyFunctions")

package com.appmattus.kotlinfixture.kotest

import com.appmattus.kotlinfixture.Fixture
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyContext
import io.kotest.property.forAll

// 1 parameter

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A) -> Boolean
) = forAll(kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A) -> Boolean
) = forAll(iterations, kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A) -> Boolean
) = forAll(config, kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A) -> Boolean
) = forAll(iterations, config, kotestGen(), function)

// 2 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), function)

// 3 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), function)

// 4 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

// 5 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

// 6 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] returns true `forAll`.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)
