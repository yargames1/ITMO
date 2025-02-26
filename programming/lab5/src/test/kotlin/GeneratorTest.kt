import io.mockk.*
import labWorkClass.LabWorkCollection
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class GeneratorTest {

    @Test
    fun `newId should return max ID + 1`() {
        mockkObject(LabWorkCollection)
        every { LabWorkCollection.getMaxId() } returns 10

        val newId = Generator.newId()

        assertEquals(11, newId)
        verify { LabWorkCollection.getMaxId() }

        unmockkObject(LabWorkCollection)
    }

    @Test
    fun `newDate should return current date`() {
        val expectedDate = LocalDate.now()

        val actualDate = Generator.newDate()

        assertEquals(expectedDate, actualDate)
    }
}
