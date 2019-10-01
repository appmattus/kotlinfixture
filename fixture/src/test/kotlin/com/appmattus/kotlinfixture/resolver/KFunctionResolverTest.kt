package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.TestContext
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
    private val context = TestContext(
        Configuration(),
        CompositeResolver(PrimitiveResolver(), StringResolver(), KTypeResolver(), KFunctionResolver())
    )

    @Test
    fun `Unknown class returns Unresolved`() {
        val result = context.resolve(Number::class)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Constructor creates instance`() {
        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = context.resolve(request)

        assertNotNull(result)
        assertEquals(SimpleClass::class, result::class)
    }

    @Test
    fun `Calls function on object`() {
        val request = KFunctionRequest(SimpleObject::class, SimpleObject::set)

        assertFalse(SimpleObject.isInitialised)

        context.resolve(request)

        assertTrue(SimpleObject.isInitialised)
    }

    @Test
    fun `Random values returned`() {
        val request = KFunctionRequest(SimpleClass::class, SimpleClass::class.primaryConstructor!!)

        assertIsRandom {
            val result = context.resolve(request) as SimpleClass
            result.value
        }
    }

    @Test
    fun `Constructor creates instance with provided parameter`() {
        val context = context.copy(
            configuration = Configuration(properties = mapOf(SimpleClass::class to mapOf("value" to "custom")))
        )

        val constructor = SimpleClass::class.primaryConstructor!!
        val request = KFunctionRequest(SimpleClass::class, constructor)

        val result = context.resolve(request) as SimpleClass

        assertEquals("custom", result.value)
    }

    @Test
    fun `Constructor with unresolvable parameter fails`() {

        val constructor = UnresolvableClass::class.primaryConstructor!!
        val request = KFunctionRequest(UnresolvableClass::class, constructor)

        val result = context.resolve(request)

        assertEquals(Unresolved, result)
    }

    @Test
    fun `Constructor with nullable parameter is sometimes null`() {
        val constructor = NullableClass::class.primaryConstructor!!
        val request = KFunctionRequest(NullableClass::class, constructor)

        assertIsRandom {
            val result = context.resolve(request) as NullableClass
            result.value == null
        }
    }

    @Test
    fun `Constructor with multiple parameters returns`() {
        val constructor = MultiParamsClass::class.primaryConstructor!!
        val request = KFunctionRequest(MultiParamsClass::class, constructor)

        val result = context.resolve(request)

        assertNotNull(result)
        assertEquals(MultiParamsClass::class, result::class)
    }

    data class NullableClass(val value: String?)

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
