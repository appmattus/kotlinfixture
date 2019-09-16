import com.flextrade.jfixture.JFixture
import com.flextrade.jfixture.NoSpecimen
import com.flextrade.jfixture.SpecimenBuilder
import com.flextrade.jfixture.SpecimenContext
import com.flextrade.jfixture.requests.GenericConstructorRequest
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

@KotlinFixtureMarker
class KotlinFixtureBuilder(val fixture: JFixture = JFixture()) {

    init {
        /*fixture.addBuilderToStartOfPipeline(object : SpecimenBuilder {
            override fun create(request: Any, context: SpecimenContext): Any {
                if (request is GenericConstructorRequest) {

                    val constructor = request.constructor

                    if (constructor.genericParameterTypes.any { (it is ParameterizedType) && it.actualTypeArguments.any { it is WildcardType } }) {
                        // has wildcard...
                        return context.resolve(GenericConstructorRequest(constructor.replaceUpperBoundsWildcardTypes(), request.containingType))
                    }
                }

                return NoSpecimen()
            }

        })*/


        fixture.addBuilderToEndOfPipeline(ObjectBuilder())
        fixture.addBuilderToEndOfPipeline(SealedClassBuilder(fixture))
    }

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

    fun build() = KotlinFixture(fixture)
}
