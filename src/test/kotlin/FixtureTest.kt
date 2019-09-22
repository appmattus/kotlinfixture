import com.appmattus.kotlinfixture.KotlinFixture
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
    fun `can create double`() {
        val double = fixture<Double>()
        assertEquals(Double::class, double::class)
    }

    @Test
    fun `can create list of Strings`() {
        val list = fixture<List<String>>()

        println(list)
    }

    @Test
    fun `can create array of Strings`() {
        val list = fixture<Array<String>>()

        println(list)
    }
}
