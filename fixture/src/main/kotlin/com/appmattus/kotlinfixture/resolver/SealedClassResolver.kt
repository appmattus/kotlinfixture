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

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.createUnresolved
import kotlin.reflect.KClass

internal class SealedClassResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if ((obj as? KClass<*>)?.isSealed == true) {
            obj.sealedSubclasses.shuffled().forEach { subclass ->
                val result = context.resolve(subclass)
                if (result is Unresolved) {
                    return createUnresolved("Unable to resolve sealed class $obj subclass $subclass", listOf(result))
                }
                return result
            }
        }

        return Unresolved.Unhandled
    }
}
