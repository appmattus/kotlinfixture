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

package com.appmattus.kotlinfixture.kotest

import com.appmattus.kotlinfixture.Fixture
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyContext
import io.kotest.property.forAll

// 1 parameter

suspend inline fun <reified A> Fixture.forAll(
    noinline fn: PropertyContext.(a: A) -> Boolean
) = forAll(kotestGen(), fn)

suspend inline fun <reified A> Fixture.forAll(
    iterations: Int,
    noinline fn: PropertyContext.(a: A) -> Boolean
) = forAll(iterations, kotestGen(), fn)

suspend inline fun <reified A> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: PropertyContext.(a: A) -> Boolean
) = forAll(config, kotestGen(), fn)

suspend inline fun <reified A> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: PropertyContext.(a: A) -> Boolean
) = forAll(iterations, config, kotestGen(), fn)

// 2 parameters

suspend inline fun <reified A, reified B> Fixture.forAll(
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.forAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), fn)

// 3 parameters

suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), fn)

// 4 parameters

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

// 5 parameters

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

// 6 parameters

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.forAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Boolean
) = forAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)
