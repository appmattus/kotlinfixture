import com.flextrade.jfixture.JFixture
import com.flextrade.jfixture.NoSpecimen
import com.flextrade.jfixture.SpecimenBuilder
import com.flextrade.jfixture.SpecimenContext
import com.flextrade.jfixture.utility.SpecimenType
import kotlin.random.Random

class SealedClassBuilder(private val fixture: JFixture) : SpecimenBuilder {
    override fun create(request: Any, context: SpecimenContext): Any {
        if (request !is SpecimenType<*>) {
            return NoSpecimen()
        }

        val kclass = request.rawType.kotlin
        if (kclass.isSealed) {
            val randomInstance = kclass.sealedSubclasses[Random.nextInt(kclass.sealedSubclasses.size)]
            return fixture.create(SpecimenType.of(randomInstance.java))
        }

        return NoSpecimen()
    }
}
