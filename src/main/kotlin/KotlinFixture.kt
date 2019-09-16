import com.flextrade.jfixture.JFixture

class KotlinFixture(val fixture: JFixture) {
    inline fun <reified T> create(range: Iterable<T> = emptyList()): T {
        val value = range.shuffled().firstOrNull()

        return if (value != null) {
            fixture.create().fromList(value)
        } else {
            fixture.create(T::class.java)
        }
    }

    inline operator fun <reified T> invoke(range: Iterable<T> = emptyList()): T {
        return create(range)
    }
}
