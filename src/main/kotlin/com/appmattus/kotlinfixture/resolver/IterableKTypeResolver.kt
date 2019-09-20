package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.config.Configuration
import java.util.AbstractQueue
import java.util.AbstractSequentialList
import java.util.ArrayDeque
import java.util.Deque
import java.util.LinkedList
import java.util.NavigableSet
import java.util.PriorityQueue
import java.util.Queue
import java.util.SortedSet
import java.util.Stack
import java.util.TreeSet
import java.util.Vector
import kotlin.reflect.KClass
import kotlin.reflect.KType

class IterableKTypeResolver(private val configuration: Configuration) : Resolver {
    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            /*if (obj.isMarkedNullable && Random.nextBoolean()) {
                null
            } else {*/

            val repeatCount = configuration.repeatCount()

            val collection = when (obj.classifier as KClass<*>) {

                Iterable::class,
                Collection::class,
                List::class,
                ArrayList::class -> ArrayList()

                java.util.AbstractCollection::class,
                AbstractList::class,
                AbstractSequentialList::class,
                LinkedList::class -> LinkedList()

                Vector::class -> Vector()

                Stack::class -> Stack()

                Queue::class,
                Deque::class,
                ArrayDeque::class -> ArrayDeque()

                AbstractQueue::class,
                PriorityQueue::class -> PriorityQueue()

                java.util.AbstractSet::class,
                Set::class,
                SortedSet::class,
                NavigableSet::class,
                TreeSet::class -> TreeSet()

                HashSet::class,
                LinkedHashSet::class -> LinkedHashSet()

                else -> {
                    @Suppress("USELESS_CAST")
                    null as MutableCollection<Any?>?
                }
            }

            if (collection != null) {
                val argType = obj.arguments.first().type

                repeat(repeatCount) {
                    collection.add(resolver.resolve(argType, resolver))
                }

                println(collection::class)
                println(collection)

                return collection
            }
        }

        return Unresolved
    }
}
