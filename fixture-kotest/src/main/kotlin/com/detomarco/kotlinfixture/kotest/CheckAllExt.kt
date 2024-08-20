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

@file:Suppress("TooManyFunctions")

package com.detomarco.kotlinfixture.kotest

import com.detomarco.kotlinfixture.Fixture
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyContext
import io.kotest.property.checkAll

// 1 parameter

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A) -> Unit
) = checkAll(kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A) -> Unit
) = checkAll(iterations, kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A) -> Unit
) = checkAll(config, kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A) -> Unit
) = checkAll(iterations, config, kotestGen(), function)

// 2 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), function)

// 3 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), function)

// 4 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

// 5 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

// 6 parameters

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    iterations: Int,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)

/**
 * Use [Fixture] to generate random objects for type parameters and validate the [function] has no assertion failures.
 */
suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline function: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), function)
