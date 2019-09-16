import com.flextrade.jfixture.NoSpecimen
import com.flextrade.jfixture.SpecimenBuilder
import com.flextrade.jfixture.SpecimenContext
import com.flextrade.jfixture.utility.SpecimenType

class ObjectBuilder : SpecimenBuilder {
    override fun create(request: Any?, context: SpecimenContext?): Any {
        if (request !is SpecimenType<*>) {
            return NoSpecimen()
        }

        return request.rawType.kotlin.objectInstance ?: NoSpecimen()
    }
}
