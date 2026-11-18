/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.Unresolved.Companion.createUnresolved
import com.appmattus.kotlinfixture.decorator.nullability.wrapNullability
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
import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TransferQueue
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal class IterableKTypeResolver : Resolver {

    @Suppress("ReturnCount")
    override fun resolve(context: Context, obj: Any): Any? {
        if (obj is KType && obj.classifier is KClass<*>) {
            val collection = createCollection(obj)

            if (collection != null) {
                return context.wrapNullability(obj) {
                    populateCollection(obj, collection)
                }
            }
        }

        return Unresolved.Unhandled
    }

    private fun Context.populateCollection(obj: KType, collection: MutableCollection<Any?>): Any {
        val argType = obj.arguments.first().type!!

        repeat(configuration.repeatCount()) {
            val value = resolve(argType)
            if (value is Unresolved) {
                return createUnresolved("Unable to resolve ${obj.classifier} argument $argType", listOf(value))
            }

            collection.add(value)
        }

        return collection
    }

    @Suppress("ComplexMethod")
    private fun createCollection(
        obj: KType
    ): MutableCollection<Any?>? = when ((obj.classifier as KClass<*>).javaObjectType) {
        Iterable::class.java,
        Collection::class.java,
        List::class.java,
        ArrayList::class.java -> ArrayList()

        java.util.AbstractCollection::class.java,
        java.util.AbstractList::class.java,
        AbstractList::class.java,
        AbstractSequentialList::class.java,
        LinkedList::class.java -> LinkedList()

        Vector::class.java -> Vector()

        Stack::class.java -> Stack()

        Queue::class.java,
        Deque::class.java,
        ArrayDeque::class.java -> ArrayDeque()

        AbstractQueue::class.java,
        PriorityQueue::class.java -> PriorityQueue()

        SortedSet::class.java,
        NavigableSet::class.java,
        TreeSet::class.java -> TreeSet()

        java.util.AbstractSet::class.java,
        Set::class.java,
        HashSet::class.java,
        LinkedHashSet::class.java -> LinkedHashSet()

        BlockingQueue::class.java,
        TransferQueue::class.java,
        LinkedTransferQueue::class.java -> LinkedTransferQueue()

        BlockingDeque::class.java,
        LinkedBlockingDeque::class.java -> LinkedBlockingDeque()

        ConcurrentLinkedDeque::class.java -> ConcurrentLinkedDeque()
        ConcurrentSkipListSet::class.java -> ConcurrentSkipListSet()
        CopyOnWriteArraySet::class.java -> CopyOnWriteArraySet()
        CopyOnWriteArrayList::class.java -> CopyOnWriteArrayList()
        ConcurrentLinkedQueue::class.java -> ConcurrentLinkedQueue()
        DelayQueue::class.java ->
            @Suppress("UNCHECKED_CAST")
            (DelayQueue<Delayed>() as MutableCollection<Any?>)
        LinkedBlockingQueue::class.java -> LinkedBlockingQueue()
        PriorityBlockingQueue::class.java -> PriorityBlockingQueue()

        else -> null
    }
}
