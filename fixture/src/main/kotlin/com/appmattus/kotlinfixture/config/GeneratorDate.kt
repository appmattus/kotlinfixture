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

package com.appmattus.kotlinfixture.config

import java.util.Date

fun Generator<Date>.before(before: Date) = Date(random.nextLong(0L, before.time))

fun Generator<Date>.after(after: Date) = Date(random.nextLong(after.time, Long.MAX_VALUE))

fun Generator<Date>.between(start: Date, end: Date) = Date(random.nextLong(start.time, end.time))
