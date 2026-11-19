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

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Classes
import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.Unresolved.Companion.createUnresolved
import io.github.classgraph.ClassInfo
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError

internal class AbstractClassResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if ((obj as? KClass<*>)?.isAbstract == true) {
            val classes = Classes.implementationsOf(obj.java)
                ?: return createUnresolved("Unable to find subclasses of abstract class $obj")

            val results = classes.shuffled(context.random).map { implementation ->
                val result = context.resolveImplementation(obj.java, implementation)

                if (result !is Unresolved) {
                    return result
                }

                result
            }

            return createUnresolved("Unable to create abstract class", results)
        }

        return Unresolved.Unhandled
    }

    /**
     * Implementations can be unusable even when they are visible, for example when one of their dependencies is
     * missing, or kotlin-reflect cannot read them. These fail the same way every time, so they are removed from the
     * implementations of [abstractClass], and treated as unresolved so another implementation is tried.
     */
    private fun Context.resolveImplementation(abstractClass: Class<*>, implementation: ClassInfo): Any? {
        val unusableReason = try {
            // loadClass(true) returns null when the class cannot be loaded, such as when a dependency is missing
            implementation.loadClass(true)?.let { clazz -> return resolve(clazz.kotlin) }
            "Unable to load $implementation"
        } catch (expected: LinkageError) {
            "Unable to use $implementation: $expected"
        } catch (expected: KotlinReflectionInternalError) {
            "Unable to use $implementation: $expected"
        }

        Classes.removeUnusableImplementation(abstractClass, implementation)
        return Unresolved.NotSupported(unusableReason)
    }
}
