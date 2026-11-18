/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture

/**
 * Packages internal to JDK 8, with their subpackages, that [Classes.internalPackages] does not already cover. Used when
 * the JDK's modules cannot be read, before Java 9, so the scan skips the same internal packages as on later versions.
 *
 * Taken from the internal packages listed at https://cr.openjdk.org/~mr/jigsaw/jdk8-packages-strongly-encapsulated,
 * leaving out those under [Classes.internalPackages] and those under another package in this list.
 */
internal val java8InternalPackages = listOf(
    "com.sun.accessibility.internal.resources",
    "com.sun.beans",
    "com.sun.crypto.provider",
    "com.sun.imageio.plugins.bmp",
    "com.sun.imageio.plugins.common",
    "com.sun.imageio.plugins.gif",
    "com.sun.imageio.plugins.jpeg",
    "com.sun.imageio.plugins.png",
    "com.sun.imageio.plugins.wbmp",
    "com.sun.imageio.spi",
    "com.sun.imageio.stream",
    "com.sun.java.swing",
    "com.sun.java_cup.internal.runtime",
    "com.sun.jmx.defaults",
    "com.sun.jmx.interceptor",
    "com.sun.jmx.mbeanserver",
    "com.sun.jmx.remote.internal",
    "com.sun.jmx.remote.protocol.rmi",
    "com.sun.jmx.remote.security",
    "com.sun.jmx.remote.util",
    "com.sun.jndi.dns",
    "com.sun.jndi.ldap",
    "com.sun.jndi.rmi.registry",
    "com.sun.jndi.toolkit.ctx",
    "com.sun.jndi.toolkit.dir",
    "com.sun.jndi.toolkit.url",
    "com.sun.jndi.url.dns",
    "com.sun.jndi.url.ldap",
    "com.sun.jndi.url.ldaps",
    "com.sun.jndi.url.rmi",
    "com.sun.media.sound",
    "com.sun.naming.internal",
    "com.sun.rmi.rmid",
    "com.sun.rowset",
    "com.sun.security.ntlm",
    "com.sun.security.sasl",
    "com.sun.swing.internal.plaf.basic.resources",
    "com.sun.swing.internal.plaf.metal.resources",
    "com.sun.swing.internal.plaf.synth.resources",
    "com.sun.tools.classfile",
    "com.sun.tools.doclint",
    "com.sun.tools.example.debug.expr",
    "com.sun.tools.example.debug.tty",
    "com.sun.tools.javac.api",
    "com.sun.tools.javac.code",
    "com.sun.tools.javac.comp",
    "com.sun.tools.javac.file",
    "com.sun.tools.javac.jvm",
    "com.sun.tools.javac.main",
    "com.sun.tools.javac.model",
    "com.sun.tools.javac.parser",
    "com.sun.tools.javac.processing",
    "com.sun.tools.javac.resources",
    "com.sun.tools.javac.tree",
    "com.sun.tools.javac.util",
    "com.sun.tools.javap",
    "com.sun.tools.jdeps",
    "com.sun.tools.jdi",
    "com.sun.tools.script.shell",
    "com.sun.xml.internal.stream",
    "java.awt.dnd.peer",
    "java.awt.peer",
    "jdk.jfr.events",
    "org.jcp.xml.dsig.internal"
)
