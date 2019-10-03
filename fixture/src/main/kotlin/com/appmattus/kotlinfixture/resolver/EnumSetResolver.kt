package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.EnumSet
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

class EnumSetResolver : Resolver {

    private val enumSetOf by lazy {
        EnumSet::class.members.first { it.name == "of" && it.parameters.size == 2 && it.parameters[1].isVararg }
    }

    private val enumSetNoneOf by lazy {
        EnumSet::class.members.first { it.name == "noneOf" }
    }

    @Suppress("ReturnCount", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                return null
            }

            if (obj.classifier == EnumSet::class) {
                val argType = obj.arguments.first().type!!
                val enumClass = argType.classifier as KClass<*>

                val allValues = (enumClass.members.first { it.name == "values" }.call() as Array<*>).toMutableList()

                val selected = mutableListOf<Any>()

                repeat(Random.nextInt(allValues.size + 1)) {
                    val index = Random.nextInt(allValues.size)
                    selected.add(allValues.removeAt(index) as Any)
                }

                return if (selected.isNotEmpty()) {
                    val last = selected.subList(1, selected.size).filterIsInstance<Enum<*>>().toTypedArray()
                    enumSetOf.call(selected.first(), last)
                } else {
                    enumSetNoneOf.call(enumClass.java) as EnumSet<*>
                }
            }
        }

        return Unresolved
    }
}
