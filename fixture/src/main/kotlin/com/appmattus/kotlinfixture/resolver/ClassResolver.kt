package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass

internal class ClassResolver : Resolver, PopulateInstance {

    @Suppress("NestedBlockDepth")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {
            val callContext = PopulateInstance.CallContext(
                context = context,
                obj = obj,
                constructorParameterNames = obj.constructorParameterNames(),
                overrides = context.configuration.properties.getOrDefault(obj, emptyMap())
            )

            obj.constructors.shuffled().forEach { constructor ->
                val result = context.resolve(KFunctionRequest(obj, constructor))
                if (result != Unresolved) {
                    return if (populatePropertiesAndSetters(callContext, result)) {
                        result
                    } else {
                        Unresolved
                    }
                }
            }
        }

        return Unresolved
    }
}
