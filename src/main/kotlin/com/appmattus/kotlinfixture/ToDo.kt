package com.appmattus.kotlinfixture

@Suppress("unused")
object ToDo {
    // TODO Factory method construction - find static functions that return instance of the class?

    // TODO Iterables - Iterable.isAssignableFrom(type) & is interface, then return mutable list, with rand no elements
    // LinkedHashMap<K, V> = java.util.LinkedHashMap<K, V>
    // HashMap<K, V> = java.util.HashMap<K, V>

    // TODO Any::class - return random class

    // TODO Maps - Map.isAssignableFrom(type) & is interface, then return mutable map, with rand no elements

    // TODO Queue - Queue.isAssignableFrom(type) & is interface, then return ArrayDeque

    // TODO Seeded requests - basically a way to ensure consistent execution

    // TODO Set - Set.isAssignableFrom(type) & is interface, the return HashSet

    // TODO Customisations:
    // TODO - instance<T : Any?>(factory: Boolean, () -> T)
    // TODO - fixed subtype - useSubType<Iterable, LinkedList>()
    // TODO - propertyOf<Order>("fieldName", 123)
    // TODO - repeatCount = 5

    // TODO handle & detect circular dependencies, A has property B, B has property A etc. See circularDependencyBehaviour

    // TODO handle unresolvable, throw exception or return null. See noResolutionBehaviour


    // TODO Check out competition projects
    // - https://github.com/marcellogalhardo/kotlin-fixture/tree/develop

    // TODO Add nullable support to getKType
    // - https://github.com/papsign/Ktor-OpenAPI-Generator/blob/2cb2d2eb8286e6ec459e7d63de9695fef31714c8/src/main/kotlin/com/papsign/kotlin/reflection/Reflection.kt
    //

    /*
        inline fun <reified T> instance(factory: Boolean = false, crossinline func: () -> T) {
        if (!factory) {
            fixture.customise().sameInstance(T::class.java, func())
        } else {
            fixture.customise().lazyInstance(T::class.java) { func() }
        }
    }

    inline fun <reified T, reified U : T> subType() =
        fixture.customise().useSubType(T::class.java, U::class.java)

    inline fun <reified T> propertyOf(name: String, value: Any) =
        fixture.customise().propertyOf(T::class.java, name, value)

    fun repeatCount(count: Int) = fixture.customise().repeatCount(count)
     */
}
