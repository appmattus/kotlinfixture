package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters

internal class ClassResolver : Resolver {

    private data class CallContext(
        val context: Context,
        val obj: KClass<*>,
        val constructorParameterNames: Set<String?>,
        val overrides: Map<String, () -> Any?>
    )

    @Suppress("NestedBlockDepth")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KClass<*>) {
            val callContext = CallContext(
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

    private fun populatePropertiesAndSetters(
        callContext: CallContext,
        result: Any?
    ): Boolean {
        return try {
            populateKotlinProperties(callContext, result) &&
                    populateJavaSetters(callContext, result)
        } catch (expected: Error) {
            // If retrieving setters fails we fail regardless of constructor used
            false
        }
    }

    private fun populateJavaSetters(
        callContext: CallContext,
        result: Any?
    ): Boolean {
        callContext.obj.memberFunctions.filter {
            it.name.startsWith("set") && it.valueParameters.size == 1
        }.filterNot {
            val name = it.name.removePrefix("set").decapitalize()
            callContext.constructorParameterNames.contains(name)
        }.forEach {
            val propertyResult = if (it.name in callContext.overrides) {
                callContext.overrides[it.name]?.invoke()
            } else {
                callContext.context.resolve(it.valueParameters[0].type)
            }

            if (propertyResult == Unresolved) {
                return false
            }

            it.safeCall(result, propertyResult)
        }

        return true
    }

    private fun populateKotlinProperties(
        callContext: CallContext,
        result: Any?
    ): Boolean {
        callContext.obj.settableMutableProperties()
            .filterNot { callContext.constructorParameterNames.contains(it.name) }
            .forEach { property ->
                val propertyResult = if (property.name in callContext.overrides) {
                    callContext.overrides[property.name]?.invoke()
                } else {
                    callContext.context.resolve(property.returnType)
                }

                if (propertyResult == Unresolved) {
                    return false
                }

                // Property might not be visible
                property.setter.safeCall(result, propertyResult)
            }

        return true
    }

    private fun KClass<*>.settableMutableProperties(): List<KMutableProperty<*>> {
        return memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.setter.visibility != KVisibility.PRIVATE }
    }

    private fun KClass<*>.constructorParameterNames(): Set<String?> {
        return constructors.flatMap { constructor ->
            constructor.parameters.map { it.name }
        }.toSet()
    }

    private fun KMutableProperty.Setter<*>.safeCall(obj: Any?, value: Any?) {
        try {
            call(obj, value)
        } catch (expected: Exception) {
            // ignored
        }
    }

    private fun KFunction<*>.safeCall(obj: Any?, value: Any?) {
        try {
            call(obj, value)
        } catch (expected: Exception) {
            // ignored
        }
    }
}
