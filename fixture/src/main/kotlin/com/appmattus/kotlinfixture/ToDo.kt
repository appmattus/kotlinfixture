package com.appmattus.kotlinfixture

@Suppress("unused")
object ToDo {
    // TODO Factory method construction - find static functions that return instance of the class?

    // TODO SynchronousQueue - adding elements to this makes little sense
    // TODO ArrayBlockingQueue - needs a fixed capacity

    // TODO EnumMap - requires special handling

    // TODO Any::class - return random class

    // TODO Seeded requests - basically a way to ensure consistent execution

    // TODO Customisations:
    // TODO - instance<T : Any?>(factory: Boolean, () -> T)
    // TODO - fixed subtype - useSubType<Iterable, LinkedList>()
    // TODO - propertyOf<Order>("fieldName", 123)
    // TODO - repeatCount = 5

    // TODO handle & detect circular dependencies, A has property B, B has property A etc. See circularDependencyBehaviour

    // TODO handle unresolvable, throw exception or return null. See noResolutionBehaviour

    // TODO Check out competition projects
    // - https://github.com/FlexTradeUKLtd/kfixture
    // - https://github.com/marcellogalhardo/kotlin-fixture/tree/develop
    // - https://blog.kotlin-academy.com/creating-a-random-instance-of-any-class-in-kotlin-b6168655b64a

    // TODO Make everything internal that should be internal

    // TODO Add tests:
    // TODO - Test configuration overloading in Configuration
    // TODO - Test ConfigurationBuilder
    // TODO - Test DateSpecification

    // TODO Write readme/documentation
    // TODO Setup CI
    // TODO Setup jcenter publishing
    // TODO Setup detekt, ktlint, owasp dependency checker

    // TODO Remove println statements

    // TODO Move this list to github issues?

    // TODO Rename library? fikstür or fiksture
}
