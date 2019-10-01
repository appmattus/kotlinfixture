package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.resolver.CompositeResolver
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.Test
import kotlin.test.assertTrue

class FixtureTest {

    @Test
    fun containsAllResolvers() {

        val actualResolvers = kotlinFixture().baseResolver
            .toList()
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

    private val Fixture.baseResolver: CompositeResolver
        get() {
            val property = Fixture::class.memberProperties.first { it.name == "baseResolver" }.apply {
                isAccessible = true
            }

            return property.get(this) as CompositeResolver
        }
}
