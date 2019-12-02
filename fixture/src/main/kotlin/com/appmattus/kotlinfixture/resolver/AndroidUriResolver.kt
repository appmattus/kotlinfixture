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

import android.net.Uri
import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.createUnresolved
import com.appmattus.kotlinfixture.typeOf
import java.net.URI

internal class AndroidUriResolver : Resolver {

    override fun resolve(context: Context, obj: Any): Any? {
        return if (hasAndroid) {
            when (obj) {
                Uri::class -> context.generateUri()
                else -> Unresolved.Unhandled
            }
        } else {
            Unresolved.Unhandled
        }
    }

    private fun Context.generateUri(): Any? {
        val oldUri = resolve(typeOf<URI>()) as? URI

        return oldUri?.let {
            Uri.Builder()
                .scheme(oldUri.scheme)
                .encodedAuthority(oldUri.rawAuthority)
                .encodedPath(oldUri.rawPath)
                .query(oldUri.rawQuery)
                .fragment(oldUri.rawFragment)
                .build()
        } ?: createUnresolved("Unable to resolve java.net.URI")
    }

    companion object {
        private val hasAndroid: Boolean by lazy {
            try {
                Class.forName("android.net.Uri", false, AndroidUriResolver::class.java.classLoader)
                true
            } catch (expected: ClassNotFoundException) {
                false
            }
        }
    }
}
