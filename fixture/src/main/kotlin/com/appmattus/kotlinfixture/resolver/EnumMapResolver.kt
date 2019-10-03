package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import java.util.EnumMap
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class EnumMapResolver : Resolver {

    private val enumMapConstructor by lazy {
        EnumMap::class.constructors.first {
            it.parameters.size == 1 &&
                    it.parameters[0].type.classifier?.starProjectedType == Class::class.starProjectedType
        }
    }

    private val enumMapPut by lazy {
        EnumMap::class.members.first { it.name == "put" }
    }

    @Suppress("ReturnCount", "ComplexMethod")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            if (obj.isMarkedNullable && Random.nextBoolean()) {
                return null
            }

            if (obj.classifier == EnumMap::class) {
                val argType = obj.arguments.first().type!!
                val enumClass = argType.classifier as KClass<*>
                val valueClass = obj.arguments[1].type?.classifier as KClass<*>

                val enumMap = enumMapConstructor.call(enumClass.java)

                val allValues = (enumClass.members.first { it.name == "values" }.call() as Array<*>).toMutableList()

                // Verify the value class can be resolved
                if (context.resolve(valueClass) == Unresolved) {
                    return Unresolved
                }

                repeat(Random.nextInt(allValues.size + 1)) {
                    val index = Random.nextInt(allValues.size)

                    val key = allValues.removeAt(index)
                    val value = context.resolve(valueClass)

                    if (value == Unresolved) {
                        return Unresolved
                    }

                    enumMapPut.call(enumMap, key, value)
                }

                return enumMap
            }
        }

        return Unresolved
    }
}
