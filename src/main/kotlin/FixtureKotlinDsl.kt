import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.resolver.ChainResolver
import com.appmattus.kotlinfixture.resolver.KTypeResolver
import com.appmattus.kotlinfixture.resolver.ObjectResolver
import com.appmattus.kotlinfixture.resolver.Resolver
import com.appmattus.kotlinfixture.resolver.SealedClassResolver
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@JvmSynthetic
fun kotlinFixture(init: KotlinFixtureBuilder.() -> Unit = {}) = KotlinFixtureBuilder().apply(init).build()

data class Something(val data: List<Sealed>)

class Order {
    var symbol: String? = null
    var size: Int = 0
    var price: BigDecimal? = null
    var previousOwners: Iterable<String>? = null

    override fun toString(): String {
        return "symbol=$symbol, size=$size, price=$price, previousOwners=$previousOwners"
    }
}

data class Order2(
    var symbol: String? = null,
    var size: Int = 0,
    var price: BigDecimal? = null,
    var previousOwners: List<@JvmSuppressWildcards BigDecimal>? = null
) : Sealed()

class WildcardParameterizedType(private val delegate: ParameterizedType) : ParameterizedType by delegate {
    override fun getActualTypeArguments(): Array<Type> {
        return delegate.actualTypeArguments.map { type ->
            if (type is WildcardType && type.upperBounds.size == 1 && type.lowerBounds.isEmpty()) {
                type.upperBounds[0]
            } else {
                type
            }
        }.toTypedArray()
    }
}

fun Constructor<*>.replaceUpperBoundsWildcardTypes(): Constructor<*> {
    val genericInfoMember = Constructor::class.declaredMembers.first { it.name == "getGenericInfo" }
    genericInfoMember.isAccessible = true

    val genericInfo = genericInfoMember.call(this)!!

    // initialise lazy data
    val getParameterTypesMember =
        genericInfo::class.declaredMembers.first { it.name == "getParameterTypes" }
    getParameterTypesMember.call(genericInfo)

    @Suppress("UNCHECKED_CAST")
    val parameterTypesMember =
        genericInfo::class.declaredMembers.first { it.name == "parameterTypes" } as KMutableProperty<Array<Type>>
    parameterTypesMember.isAccessible = true
    val parameters = parameterTypesMember.call(genericInfo)

    val newParams = parameters.map { parameterizedType ->
        if (parameterizedType is ParameterizedType) {
            WildcardParameterizedType(parameterizedType)
        } else {
            parameterizedType
        }
    }.toTypedArray()

    parameterTypesMember.setter.call(genericInfo, newParams)

    return this
}

sealed class None

open class A
open class B : A()
open class C : B()

data class Config(val repeatCount: () -> Int)

val config = Config { Random.nextInt(5) }

val classGraph: ScanResult = ClassGraph().enableSystemJarsAndModules().enableClassInfo().scan()

class IterableResolver : Resolver {
    override fun resolve(obj: Any?, resolver: Resolver): Any? {
        if (obj is KType) {
            if (obj.classifier == List::class) {

                val itemType = obj.arguments.first()

                if (itemType.type == null && itemType.variance == null) {
                    throw UnsupportedOperationException("Unable to handle star projection")
                }

                // It's a list so projection is always out, i.e. List<? extends E>
                val qualifiedName = (itemType.type?.classifier as KClass<*>).qualifiedName
                println(qualifiedName)

                println(classGraph.getSubclasses(qualifiedName).map { it.loadClass() })


                // KTypeProjection

                println("Type: " + itemType.type)
                println("Variance: " + itemType.variance)

                val items = mutableListOf<Any?>()
                repeat(config.repeatCount()) {
                    val item = resolver.resolve(itemType, resolver)
                    items.add(item)
                }

                return items.toList()
            }

            return resolver.resolve(obj.classifier, resolver)
        }
        return Unresolved
    }
}

data class Variance(val invariant: List<B>, val variantOut: List<out B>)

data class NullsAllowed(val nullable: List<B>)

fun main() {

    val resolver = ChainResolver(
        listOf(
            IterableResolver(),
            ObjectResolver(),
            SealedClassResolver(),
            KTypeResolver()
        )
    )

    println(resolver.resolve(None::class, resolver))

    println(resolver.resolve(Sealed::class, resolver))


    val clazz = NullsAllowed::class

    val primary = clazz.primaryConstructor

    println(primary)
    val firstType = primary?.parameters?.get(0)?.type!!

    println(firstType)
    //println(primary?.valueParameters)

    println("nullable type")

    //repeat(100) {
    println(resolver.resolve(firstType, resolver))

    //}


    val fixture = kotlinFixture {
        //instance(false) { Random.nextInt().toString() }

        subType<Number, Int>()

        propertyOf<Sealed.SealedData>("data", "25")
        propertyOf<Sealed.InnerSeal.InnerData>("data", 26)

        propertyOf<Order>("size", 12345)
        propertyOf<Order2>("size", 1234)


        //repeatCount(15)
    }


    //ParameterizedType()
    println(fixture<List<@JvmSuppressWildcards String>>())

    println(fixture<Something>())
    println(fixture<Something>())
    println(fixture<Something>())
    println(fixture<Something>())


    //println(fixture<Order>())
    //println(fixture<Order2>())

    repeat(1) {
        //println(fixture(listOf(1, 3, 5)))
        //println(fixture(1..5))

        //println(fixture.create(listOf(1,3,5)))

        //println(fixture.create<String>())

        //println(fixture.create(1..5))
        //println(fixture.create(1L..5L step 4L))
        //println(fixture.create(0.5f.rangeTo(1f)))


        //println(fixture<Sealed>())
        //println(fixture.createInRange(1..5))
    }
}

sealed class Sealed {
    object SealedObject : Sealed() {
        override fun toString() = "SealedObject"
    }

    sealed class InnerSeal : Sealed() {
        object InnerObject : InnerSeal() {
            override fun toString() = "InnerObject"
        }

        data class InnerData(var data: Int) : InnerSeal()
    }

    data class SealedData(var data: String) : Sealed()
}
