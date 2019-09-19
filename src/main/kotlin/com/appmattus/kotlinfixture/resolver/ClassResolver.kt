package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class ClassResolver(private val configuration: Configuration) : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KClass<*>) {

            val constructorParameterNames = obj.constructors.flatMap { constructor ->
                constructor.parameters.map { it.name }
            }.toSet()

            val overrides = configuration.properties.getOrDefault(obj, emptyMap())

            obj.constructors.shuffled().forEach { constructor ->
                try {
                    val result = resolver.resolve(KFunctionRequest(obj, constructor), resolver)
                    if (result != Unresolved) {

                        obj.memberProperties
                            .filterIsInstance<KMutableProperty<*>>()
                            .filterNot { constructorParameterNames.contains(it.name) }
                            .forEach { property ->
                                val propertyResult = overrides.getOrElse(property.name) {
                                    resolver.resolve(property.returnType, resolver)
                                }

                                if (propertyResult == Unresolved) {
                                    return Unresolved
                                }

                                property.setter.call(result, propertyResult)
                            }

                        return result
                    }
                } catch (expected: Exception) {

                }
            }
        }

        return Unresolved
    }
}

@Suppress("unused")
data class TestClass(val myValue: String = "bob", val another: String, var number: Int) {
    constructor(name: String) : this(another = "name-$name", number = 5)
    constructor(number: Int) : this(another = "number-$number", number = 5)

    val immutable: Int = 5

    var mutable: Int = 6

    private val privateMutable: Int = 7

    lateinit var lateInitMutable: String

    var nullableMutable: String? = null
}

fun main() {
    val config = Configuration(
        properties = mapOf(
            TestClass::class to mapOf(
                "myValue" to "Hey hey",
                "nullableMutable" to "not nullable"
            )
        )
    )

    val chain = CompositeResolver(KFunctionResolver(config), KTypeResolver(), StringResolver(), PrimitiveResolver())

    println(ClassResolver(config).resolve(TestClass::class, chain))
}
