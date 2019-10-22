package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.staticFunctions

@Suppress("NestedBlockDepth")
class FactoryMethodResolver : Resolver, PopulateInstance {

    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {

            (obj.companionObjectFactoryMethods() ?: obj.staticFactoryMethods()).shuffled().forEach { factoryMethod ->

                val request = KFunctionRequest(obj, factoryMethod)
                val result = context.resolve(request)

                if (result != Unresolved) {
                    if (result != null) {
                        val callContext = PopulateInstance.CallContext(
                            context,
                            result::class,
                            constructorParameterNames = emptySet(),
                            overrides = context.configuration.properties.getOrDefault(obj, emptyMap())
                        )

                        populatePropertiesAndSetters(callContext, result)
                    }

                    return result
                }
            }
        }

        return Unresolved
    }

    private fun KClass<*>.staticFactoryMethods(): List<KFunction<*>> {
        return staticFunctions.filter {
            (it.returnType.classifier as KClass<*>).isSubclassOf(this)
        }
    }

    private fun KClass<*>.companionObjectFactoryMethods(): List<KFunction<*>>? {
        return companionObject?.functions?.filter {
            (it.returnType.classifier as KClass<*>).isSubclassOf(this)
        }
    }
}
