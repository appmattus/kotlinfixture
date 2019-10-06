package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.test.Test
import kotlin.test.assertTrue

class FixtureTest {

    @Test
    fun containsAllResolvers() {

        val actualResolvers = Configuration().resolvers
            .map { it::class.java.simpleName }
            .sorted()

        val missingResolvers = Classes.classGraph.getClassInfo(Resolver::class.java.name).classesImplementing
            .map { it.simpleName }
            .filterNot { it == "TestResolver" || it == "CompositeResolver" }
            .sorted()
            .toMutableList().apply {
                removeAll(actualResolvers)
            }

        assertTrue("Missing the resolvers: $missingResolvers") {
            missingResolvers.isEmpty()
        }
    }
}
