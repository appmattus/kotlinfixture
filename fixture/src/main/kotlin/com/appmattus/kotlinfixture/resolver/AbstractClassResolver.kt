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
            val classInfo = Classes.classGraph.getClassInfo(obj.java.name)
                ?: return createUnresolved("Unable to find subclasses of abstract class $obj")

            val classes = if (classInfo.isInterface) classInfo.classesImplementing else classInfo.subclasses

            val results = classes.filterNot { it.isInternal() }.shuffled(context.random).map { subclass ->
                // loadClass(true) returns null when the class cannot be loaded, such as when a dependency is missing
                val result = subclass.loadClass(true)?.let { context.resolveOrUnresolved(it) }
                    ?: Unresolved.NotSupported("Unable to load $subclass")

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
     * missing, or kotlin-reflect cannot read them. Treat these as unresolved so another implementation is tried.
     */
    private fun Context.resolveOrUnresolved(clazz: Class<*>): Any? = try {
        resolve(clazz.kotlin)
    } catch (expected: LinkageError) {
        Unresolved.NotSupported("Unable to use $clazz: $expected")
    } catch (expected: KotlinReflectionInternalError) {
        Unresolved.NotSupported("Unable to use $clazz: $expected")
    }

    private fun ClassInfo.isInternal() = INTERNAL_PACKAGES.any { name.startsWith(it) }

    private companion object {
        // Implementation details of the JDK and kotlin-reflect, which are not meaningful values to generate
        private val INTERNAL_PACKAGES = listOf("sun.", "jdk.internal.", "kotlin.reflect.jvm.internal.")
    }
}
