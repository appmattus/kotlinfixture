package com.appmattus.kotlinfixture.kotlintest

import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import io.kotlintest.properties.Gen
import kotlin.reflect.KType

class KotlinTestGenerator(private val kotlinFixture: Fixture, private val type: KType) : Gen<Any?> {
    private val configuration = ConfigurationBuilder(kotlinFixture.fixtureConfiguration).apply {
        resolvers.add(0, KotlinTestResolver())
    }.build()

    override fun constants() = emptyList<Any?>()
    override fun random() = generateSequence {
        kotlinFixture.create(type, configuration)
    }
}
