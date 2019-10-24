package com.appmattus.kotlinfixture.decorator.logging

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.typeOf
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.internal.verification.Times
import kotlin.reflect.KType
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggingDecoratorTest {

    private val mockLoggingStrategy = mock<LoggingStrategy>()

    private val config = ConfigurationBuilder().apply {
        loggingStrategy(mockLoggingStrategy)
    }.build()

    @Test
    fun `logs each request`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Short>()))

        val decoratedResolver = LoggingDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        argumentCaptor<KType> {
            verify(mockLoggingStrategy, Times(3)).request(capture())

            assertEquals(listOf(typeOf<Float>(), typeOf<Int>(), typeOf<Short>()), allValues)
        }
    }

    @Test
    fun `logs each response`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Short>()))

        val decoratedResolver = LoggingDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockLoggingStrategy).response(typeOf<Short>(), Result.success(typeOf<Short>()))
        verify(mockLoggingStrategy).response(typeOf<Int>(), Result.success(typeOf<Short>()))
        verify(mockLoggingStrategy).response(typeOf<Float>(), Result.success(typeOf<Short>()))
    }

    class TestResolver(list: List<KType>) : Resolver {

        private val objects = list.iterator()

        override fun resolve(context: Context, obj: Any): Any? {
            return if (objects.hasNext()) {
                context.resolve(objects.next())
            } else {
                obj
            }
        }
    }
}
