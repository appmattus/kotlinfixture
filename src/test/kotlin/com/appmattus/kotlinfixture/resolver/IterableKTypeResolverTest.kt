package com.appmattus.kotlinfixture.resolver

import com.appmattus.kotlinfixture.config.Configuration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
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
import kotlin.test.Test
import kotlin.test.assertTrue

@RunWith(Parameterized::class)
class IterableKTypeResolverTest {
    private val resolver =
        CompositeResolver(
            IterableKTypeResolver(Configuration()),
            StringResolver(),
            PrimitiveResolver(),
            KTypeResolver()
        )


    // other hierarchies
    // RandomAccess <- Vector, ArrayList, CopyOnWriteArrayList
    // Map <- HashTable, HashMap, SortedMap, TreeMap
    // CopyOnWriteArraySet, EnumSet
    // ConcurrentSkipListSet

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

    object Object {
        lateinit var nested: Iterable<Iterable<Iterable<String>>>

        // Interfaces
        lateinit var iterable: Iterable<String>
        lateinit var collection: Collection<String>
        lateinit var list: List<String>
        lateinit var mutableList: MutableList<String>
        lateinit var queue: Queue<String>
        lateinit var deque: Deque<String>
        lateinit var set: Set<String>
        lateinit var sortedSet: SortedSet<String>
        lateinit var navigableSet: NavigableSet<String>

        // Abstract
        lateinit var abstractCollection: java.util.AbstractCollection<String>
        lateinit var abstractSequentialList: AbstractSequentialList<String>
        lateinit var abstractQueue: AbstractQueue<String>
        lateinit var abstractSet: java.util.AbstractSet<String>

        // Concrete
        lateinit var arrayList: ArrayList<String>
        lateinit var linkedList: LinkedList<String>
        lateinit var vector: Vector<String>
        lateinit var stack: Stack<String>
        lateinit var priorityQueue: PriorityQueue<String>
        lateinit var arrayDeque: ArrayDeque<String>
        lateinit var hashSet: HashSet<String>
        lateinit var linkedHashSet: LinkedHashSet<String>
        lateinit var treeSet: TreeSet<String>
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = arrayOf(
            arrayOf(Object::iterable.returnType, Iterable::class),
            arrayOf(Object::collection.returnType, Collection::class),
            arrayOf(Object::list.returnType, List::class),
            arrayOf(Object::mutableList.returnType, MutableList::class),
            arrayOf(Object::queue.returnType, Queue::class),
            arrayOf(Object::deque.returnType, Deque::class),
            arrayOf(Object::set.returnType, Set::class),
            arrayOf(Object::sortedSet.returnType, SortedSet::class),
            arrayOf(Object::arrayList.returnType, ArrayList::class),
            arrayOf(Object::linkedList.returnType, LinkedList::class),
            arrayOf(Object::vector.returnType, Vector::class),
            arrayOf(Object::stack.returnType, Stack::class),
            arrayOf(Object::priorityQueue.returnType, PriorityQueue::class),
            arrayOf(Object::arrayDeque.returnType, ArrayDeque::class),
            arrayOf(Object::hashSet.returnType, HashSet::class),
            arrayOf(Object::linkedHashSet.returnType, LinkedHashSet::class),
            arrayOf(Object::treeSet.returnType, TreeSet::class),
            arrayOf(Object::navigableSet.returnType, NavigableSet::class),
            arrayOf(Object::abstractCollection.returnType, java.util.AbstractCollection::class),
            arrayOf(Object::abstractSequentialList.returnType, AbstractSequentialList::class),
            arrayOf(Object::abstractQueue.returnType, AbstractQueue::class),
            arrayOf(Object::abstractSet.returnType, java.util.AbstractSet::class)
        )
    }
}
