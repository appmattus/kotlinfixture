package com.appmattus.kotlinfixture

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

object Classes {
    val classGraph: ScanResult = ClassGraph().enableSystemJarsAndModules().enableClassInfo().scan()
}
