package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible

class ClassResolver : Resolver {

    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KClass<*>) {
            val constructors = obj.constructors.shuffled()

            constructors.forEach {
                try {
                    val result = resolver.resolve(it, resolver)
                    if (result != Unresolved) {

                        // Call any public setters as appropriate that aren't included in the constructor?
                        // Especially important if this class had a default constructor


                        return result
                    }
                } catch (expected: Exception) {

                }
            }
        }

        return Unresolved
    }
}

class KFunctionResolver : Resolver {
    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KFunction<*>) {
            return try {
                obj.isAccessible = true

                // Context is useful in here for being able to pre-define values as you need to know the class of this
                // function

                val parameters = obj.parameters.associateWith {
                    resolver.resolve(it.type, resolver)
                }.filterKeys { !it.isOptional || Random.nextBoolean() }

                if (parameters.all { it.value != Unresolved }) {
                    obj.callBy(parameters)
                } else {
                    Unresolved
                }
            } catch (expected: Exception) {
                Unresolved
            }
        }

        return Unresolved
    }
}

data class TestClass(val myValue: String = "bob", val another: String, val number: Int) {
    constructor(name: String) : this(another = "hi-$name", number = 5)
    constructor(number: Number) : this(another = "hi-$number", number = 5)
}

fun main() {
    val function = TestClass::class.constructors.toList()[0]

    val chain = CompositeResolver(listOf(KFunctionResolver(), KTypeResolver(), StringResolver(), PrimitiveResolver()))

    println(ClassResolver().resolve(TestClass::class, chain))
}
