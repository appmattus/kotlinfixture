package com.appmattus.kotlinfixture.decorator.recursion

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.TestContext
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.typeOf
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.mockito.internal.verification.NoMoreInteractions
import kotlin.reflect.KType
import kotlin.test.Test

class RecursionDecoratorTest {

    private val mockRecursionStrategy = mock<RecursionStrategy>()

    private val config = ConfigurationBuilder().apply {
        recursionStrategy(mockRecursionStrategy)
    }.build()

    @Test
    fun `calls resolver when same ktype recursively requested again`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Float>()))

        val decoratedResolver = RecursionDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockRecursionStrategy).handleRecursion(typeOf<Float>(), listOf(typeOf<Float>(), typeOf<Int>()))
    }

    @Test
    fun `does not call resolver when all different ktypes recursively requested`() {
        val resolver = TestResolver(listOf(typeOf<Int>(), typeOf<Double>(), typeOf<Short>()))

        val decoratedResolver = RecursionDecorator().decorate(resolver)
        decoratedResolver.resolve(TestContext(config, decoratedResolver), typeOf<Float>())

        verify(mockRecursionStrategy, NoMoreInteractions()).handleRecursion(any(), any())
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
