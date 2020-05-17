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

@file:Suppress("unused")

package com.appmattus.kotlinfixture.kotest

import com.appmattus.kotlinfixture.Fixture
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyContext
import io.kotest.property.checkAll

// 1 parameter

suspend inline fun <reified A> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A) -> Unit
) = checkAll(kotestGen(), fn)

suspend inline fun <reified A> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A) -> Unit
) = checkAll(iterations, kotestGen(), fn)

suspend inline fun <reified A> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A) -> Unit
) = checkAll(config, kotestGen(), fn)

suspend inline fun <reified A> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A) -> Unit
) = checkAll(iterations, config, kotestGen(), fn)

// 2 parameters

suspend inline fun <reified A, reified B> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), fn)

// 3 parameters

suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), fn)

// 4 parameters

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

// 5 parameters

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

// 6 parameters

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    iterations: Int,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(iterations, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Fixture.checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline fn: suspend PropertyContext.(a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
) = checkAll(iterations, config, kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), kotestGen(), fn)
