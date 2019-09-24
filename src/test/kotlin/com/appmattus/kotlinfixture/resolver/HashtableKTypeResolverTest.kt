package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Dictionary
import java.util.Hashtable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertTrue

@RunWith(Parameterized::class)
class HashtableKTypeResolverTest {
    private val resolver =
        CompositeResolver(
            HashtableKTypeResolver(Configuration()),
            StringResolver(),
            PrimitiveResolver(),
            KTypeResolver()
        )

    @Parameterized.Parameter(0)
    lateinit var type: KType

    @Parameterized.Parameter(1)
    lateinit var resultClass: KClass<*>

    @Test
    fun `creates instance`() {
        val result = resolver.resolve(type, resolver)

        assertTrue {
            resultClass.isInstance(result)
        }
    }

    companion object {
        @JvmStatic
        @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
        @Parameterized.Parameters(name = "{1}")
        fun data() = arrayOf(
            arrayOf(typeOf<Dictionary<String, String>>(), Dictionary::class),
            arrayOf(typeOf<Hashtable<String, String>>(), Hashtable::class)
        )
    }
}
