package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.decorator.logging.LoggingDecorator
import com.appmattus.kotlinfixture.decorator.logging.SysOutLoggingStrategy
import com.appmattus.kotlinfixture.decorator.recursion.NullRecursionStrategy
import com.appmattus.kotlinfixture.decorator.recursion.RecursionDecorator
import kotlin.random.Random
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class Fixture(val fixtureConfiguration: Configuration) {

    inline operator fun <reified T : Any?> invoke(
        range: Iterable<T> = emptyList(),
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = create(typeOf<T>(), ConfigurationBuilder(fixtureConfiguration).apply(configuration).build())
            if (result is T) {
                result
            } else {
                println(result)
                throw UnsupportedOperationException("Unable to handle ${T::class}")
            }
        }
    }

    fun create(type: KType, configuration: Configuration): Any? {
        return ContextImpl(configuration).resolve(type)
    }
}

fun kotlinFixture(init: ConfigurationBuilder.() -> Unit = {}) =
    Fixture(ConfigurationBuilder().apply(init).build())

class TestClass(val bob: String) {
    override fun toString() = "TestClass [bob=$bob]"
}

class TestClass2 {
    lateinit var bob: String

    override fun toString() = "TestClass2 [bob=${if (::bob.isInitialized) bob else "uninit"}]"
}

class TestClass3 {
    fun setBob(@Suppress("UNUSED_PARAMETER") bob: String) {
        println("TestClass3 [bob=$bob]")
    }
}

fun main() {

    val fixture = kotlinFixture {
        repeatCount { 5 }

        subType<Number, Double>()

        propertyOf<TestClass>("bob") { "hello" + Random.nextInt(1, 5) }
        property(TestClass::bob) { "hi" }
        property(TestClass2::bob) { "hello" }

        instance { TestClass3().apply { setBob("hi") } }
    }

    println(fixture<List<String>>())

    fixture<List<String>> {
        repeatCount { 2 }

        decorators.add(LoggingDecorator(SysOutLoggingStrategy()))
    }

    println(fixture(listOf(1, 2, 3)))

    println(fixture<TestClass>())
    println(fixture<TestClass2>())
    println(fixture<TestClass3>())

    println(fixture<Number>())
    println(fixture<Number> {
        subType<Number, Int>()
    })

    println(fixture<A> {
        decorators.removeIf { it is RecursionDecorator }
        decorators.add(RecursionDecorator(NullRecursionStrategy()))
        decorators.add(LoggingDecorator(SysOutLoggingStrategy()))
    })
}

class A {
    lateinit var b: B

    override fun toString(): String {
        return "A[${if (::b.isInitialized) b.toString() else "uninit"}]"
    }
}

class B {
    lateinit var a: A

    override fun toString(): String {
        return "B[${if (::a.isInitialized) a.toString() else "uninit"}]"
    }
}
