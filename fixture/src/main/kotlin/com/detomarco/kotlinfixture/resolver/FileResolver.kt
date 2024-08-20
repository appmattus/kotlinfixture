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

package com.detomarco.kotlinfixture.resolver

import com.detomarco.kotlinfixture.Context
import com.detomarco.kotlinfixture.Unresolved
import com.detomarco.kotlinfixture.nextUuid
import java.io.File

internal class FileResolver : Resolver {
    override fun resolve(context: Context, obj: Any): Any? {
        return if (obj == File::class) {
            return File(context.random.nextUuid().toString())
        } else {
            Unresolved.Unhandled
        }
    }
}
