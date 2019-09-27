package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.resolver.AbstractClassResolver
import com.appmattus.kotlinfixture.resolver.ArrayResolver
import com.appmattus.kotlinfixture.resolver.BigDecimalResolver
import com.appmattus.kotlinfixture.resolver.BigIntegerResolver
import com.appmattus.kotlinfixture.resolver.CalendarResolver
import com.appmattus.kotlinfixture.resolver.CharResolver
import com.appmattus.kotlinfixture.resolver.ClassResolver
import com.appmattus.kotlinfixture.resolver.CompositeResolver
import com.appmattus.kotlinfixture.resolver.DateResolver
import com.appmattus.kotlinfixture.resolver.EnumResolver
import com.appmattus.kotlinfixture.resolver.HashtableKTypeResolver
import com.appmattus.kotlinfixture.resolver.IterableKTypeResolver
import com.appmattus.kotlinfixture.resolver.KFunctionResolver
import com.appmattus.kotlinfixture.resolver.KTypeResolver
import com.appmattus.kotlinfixture.resolver.MapKTypeResolver
import com.appmattus.kotlinfixture.resolver.ObjectResolver
import com.appmattus.kotlinfixture.resolver.PrimitiveArrayResolver
import com.appmattus.kotlinfixture.resolver.PrimitiveResolver
import com.appmattus.kotlinfixture.resolver.SealedClassResolver
import com.appmattus.kotlinfixture.resolver.StringResolver
import com.appmattus.kotlinfixture.resolver.SubTypeResolver
import com.appmattus.kotlinfixture.resolver.UriResolver
import com.appmattus.kotlinfixture.resolver.UrlResolver
import com.appmattus.kotlinfixture.resolver.UuidResolver
import kotlin.random.Random
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class Fixture(private val baseConfiguration: Configuration) {

    private val baseResolver = CompositeResolver(
        CharResolver(),
        StringResolver(),
        PrimitiveResolver(),
        UrlResolver(),
        UriResolver(),
        BigDecimalResolver(),
        BigIntegerResolver(),
        UuidResolver(),
        EnumResolver(),
        CalendarResolver(),
        DateResolver(),

        ObjectResolver(),
        SealedClassResolver(),

        ArrayResolver(),

        PrimitiveArrayResolver(),
        HashtableKTypeResolver(),
        IterableKTypeResolver(),
        MapKTypeResolver(),
        KTypeResolver(),
        KFunctionResolver(),

        SubTypeResolver(),

        AbstractClassResolver(),

        ClassResolver()
    )

    inline operator fun <reified T : Any?> invoke(
        range: Iterable<T> = emptyList(),
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val result = create(typeOf<T>(), ConfigurationBuilder().apply(configuration).build())
            if (result is T) {
                result
            } else {
                throw UnsupportedOperationException("Unable to handle ${T::class}")
            }
        }
    }

    fun create(type: KType, configuration: Configuration): Any? {
        val context = object : Context {
            override val configuration = baseConfiguration + configuration
            override val resolver = baseResolver
        }
        return context.resolve(type)
    }
}

fun kotlinFixture(init: ConfigurationBuilder.() -> Unit = {}) = Fixture(ConfigurationBuilder().apply(init).build())

class TestClass(val bob: String) {
    override fun toString() = "TestClass [bob=$bob]"
}

class TestClass2 {
    lateinit var bob: String

    override fun toString() = "TestClass2 [bob=$bob]"
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


    println(fixture<List<String>> {
        repeatCount { 2 }
    })

    println(fixture(listOf(1, 2, 3)))


    println(fixture<TestClass>())
    println(fixture<TestClass2>())
    println(fixture<TestClass3>())


    println(fixture<Number>())
    println(fixture<Number> {
        subType<Number, Int>()
    })
}
