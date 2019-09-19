package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.assertIsRandom
import com.appmattus.kotlinfixture.config.Configuration
import kotlin.reflect.full.primaryConstructor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KFunctionResolverTest {
    private val resolver = KFunctionResolver(Configuration())
    private val testResolver = CompositeResolver(PrimitiveResolver(), StringResolver(), KTypeResolver())

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = resolver.resolve(Number::class, resolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Constructor creates instance`() {
        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = resolver.resolve(request, testResolver)

        assertNotNull(result)
        assertEquals(SimpleClass::class, result::class)
    }

    @Test
    fun `Calls function on object`() {
        val request = KFunctionRequest(SimpleObject::class, SimpleObject::set)

        assertFalse(SimpleObject.isInitialised)

        resolver.resolve(request, testResolver)

        assertTrue(SimpleObject.isInitialised)
    }

    @Test
    fun `Random values returned`() {
        val request = KFunctionRequest(SimpleClass::class, SimpleClass::class.primaryConstructor!!)

        assertIsRandom {
            val result = resolver.resolve(request, testResolver) as SimpleClass
            result.value
        }
    }

    @Test
    fun `Constructor creates instance with provided parameter`() {

        val resolver = KFunctionResolver(
            Configuration(properties = mapOf(SimpleClass::class to mapOf("value" to "custom")))
        )

        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = resolver.resolve(request, testResolver) as SimpleClass

        assertEquals("custom", result.value)
    }

    @Test
    fun `Constructor with unresolvable parameter fails`() {

        val constructor = UnresolvableClass::class.primaryConstructor!!
        val request = KFunctionRequest(UnresolvableClass::class, constructor)

        val result = resolver.resolve(request, testResolver)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Constructor with optional parameter is sometimes null`() {
        val constructor = OptionalClass::class.primaryConstructor!!
        val request = KFunctionRequest(OptionalClass::class, constructor)

        assertIsRandom {
            val result = resolver.resolve(request, testResolver) as OptionalClass
            result.value == null
        }
    }

    @Test
    fun `Constructor with multiple parameters returns`() {
        val constructor = MultiParamsClass::class.primaryConstructor!!
        val request = KFunctionRequest(MultiParamsClass::class, constructor)

        val result = resolver.resolve(request, testResolver)

        assertNotNull(result)
        assertEquals(MultiParamsClass::class, result::class)
    }

    data class OptionalClass(val value: String?)

    data class UnresolvableClass(val value: Number)

    data class SimpleClass(val value: String)

    data class MultiParamsClass(val value1: String, val value2: Int)

    object SimpleObject {
        @Suppress("MemberVisibilityCanBePrivate")
        lateinit var value: String

        val isInitialised: Boolean
            get() = ::value.isInitialized

        fun set(value: String) {
            this.value = value
        }
    }
}
