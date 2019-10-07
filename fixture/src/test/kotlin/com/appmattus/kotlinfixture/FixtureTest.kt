package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.decorator.logging.LoggingDecorator
import com.appmattus.kotlinfixture.decorator.logging.SysOutLoggingStrategy
import com.appmattus.kotlinfixture.resolver.Resolver
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

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

    @Test
    fun `providing empty list has no effect`() {
        val fixture = kotlinFixture()
        assertIsRandom {
            fixture(emptyList<Int>())
        }
    }

    @Test
    fun `providing list of one returns its value`() {
        val fixture = kotlinFixture()
        repeat(100) {
            assertEquals(10, fixture(listOf(10)))
        }
    }

    @Test
    fun `providing multi-value list returns one its values`() {
        val fixture = kotlinFixture()
        repeat(100) {
            val result = fixture(listOf(10, 20, 30))

            if (result != 10 && result != 20 && result != 30) {
                fail()
            }
        }
    }

    @Test
    fun `providing multi-value list returns a random value`() {
        val fixture = kotlinFixture()
        assertIsRandom {
            fixture(listOf(10, 20, 30))
        }
    }

    @Test
    fun `repeatCount can be set in fixture initialisation`() {
        val fixture = kotlinFixture {
            repeatCount { 1 }
        }

        val list = fixture<List<String>>()
        assertEquals(1, list.size)
    }

    @Test
    fun `repeatCount can be overridden in fixture creation`() {
        val fixture = kotlinFixture()

        val list = fixture<List<String>>() {
            repeatCount { 2 }
        }
        assertEquals(2, list.size)
    }

    @Test
    fun `repeatCount can be overridden in fixture creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            repeatCount { 1 }
        }

        val list = fixture<List<String>>() {
            repeatCount { 2 }
        }
        assertEquals(2, list.size)
    }

    abstract class Superclass
    class SubclassA : Superclass()
    class SubclassB : Superclass()

    @Test
    fun `subclass is random by default for superclass class`() {
        val fixture = kotlinFixture()

        assertIsRandom {
            fixture<Superclass>()::class
        }
    }

    @Test
    fun `subclass can be overridden in initialisation`() {
        val fixture = kotlinFixture {
            subType<Superclass, SubclassA>()
        }

        repeat(100) {
            assertEquals(SubclassA::class, fixture<Superclass>()::class)
        }
    }

    @Test
    fun `subclass can be overridden in creation`() {
        val fixture = kotlinFixture()

        repeat(100) {
            val result = fixture<Superclass> {
                subType<Superclass, SubclassB>()
            }
            assertEquals(SubclassB::class, result::class)
        }
    }

    @Test
    fun `subclass can be overridden in creation when already overridden in initialisation`() {
        val fixture = kotlinFixture {
            subType<Superclass, SubclassA>()
        }

        repeat(100) {
            val result = fixture<Superclass> {
                subType<Superclass, SubclassB>()
            }
            assertEquals(SubclassB::class, result::class)
        }
    }

    @Test
    fun `can override instance in initialisation`() {
        val fixture = kotlinFixture {
            instance<Number> { 10 }
        }

        repeat(100) {
            assertEquals(10, fixture<Number>())
        }
    }

    @Test
    fun `overridden instance in initialisation is random`() {
        val fixture = kotlinFixture {
            instance<Number> { Random.nextInt(1, 5) }
        }

        repeat(100) {
            assertTrue { fixture<Number>() in 1..5 }
        }
    }

    @Test
    fun `can override instance in creation`() {
        val fixture = kotlinFixture()

        repeat(100) {
            val result = fixture<Number> {
                instance<Number> { 20 }
            }
            assertEquals(20, result)
        }
    }

    @Test
    fun `overridden instance in creation is random`() {
        val fixture = kotlinFixture()

        repeat(100) {
            val result = fixture<Number> {
                instance<Number> { Random.nextInt(6, 10) }
            }

            assertTrue { result in 6..10 }
        }
    }

    @Test
    fun `can override instance in creation when overridden in initialisation`() {
        val fixture = kotlinFixture {
            instance<Number> { 10 }
            decorators.add(LoggingDecorator(SysOutLoggingStrategy()))
        }

        repeat(100) {
            val result = fixture<Number> {
                instance<Number> { 30 }
            }
            assertEquals(30, result)
        }
    }
}
