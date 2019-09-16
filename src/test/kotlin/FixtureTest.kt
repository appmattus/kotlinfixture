import com.appmattus.kotlinfixture.KotlinFixture
import kotlin.reflect.full.createType
import kotlin.test.Test
import kotlin.test.assertEquals

class FixtureTest {

    val fixture = KotlinFixture()

    @Test
    fun `can create int`() {
        val int = fixture<Int>()
        assertEquals(Int::class, int::class)
    }

    @Test
    fun `can create int from KType`() {
        println(Int::class.createType())

        val int = fixture.create(Int::class.createType())
        //val int = fixture.create(Int::class)

        assertEquals(Int::class, int::class)
    }

    @Test
    fun `can create double`() {
        val double = fixture<Double>()
        assertEquals(Double::class, double::class)
    }
}
