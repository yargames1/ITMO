package labWorkClass

import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class LabWorkCollectionTest {

    @Test
    fun `getMaxId should return -1`() {
        mockkObject(LabWorkCollection)
        val realCollection = HashMap<String, LabWork>()
        LabWorkCollection.collection = realCollection

        val maxId = LabWorkCollection.getMaxId()
        assertEquals(-1, maxId)
        verify { LabWorkCollection.getMaxId() }

        unmockkAll()
        LabWorkCollection.collection.clear()
    }

    @Test
    fun `getMaxId should return 1`() {
        mockkObject(LabWorkCollection)
        val realCollection = HashMap<String, LabWork>()

        val labWork1 = mockk<LabWork>()
        val labWork2 = mockk<LabWork>()

        every { labWork1.getId() } returns 1
        every { labWork2.getId() } returns 0

        realCollection["1"] = labWork2 // "1" соответствует person2 ("A")
        realCollection["2"] = labWork1

        LabWorkCollection.collection = realCollection

        val maxId = LabWorkCollection.getMaxId()
        assertEquals(1, maxId)
        verify { LabWorkCollection.getMaxId() }

        unmockkAll()
        LabWorkCollection.collection.clear()
    }

    @Test
    fun `getKeyById should return 1`() {
        mockkObject(LabWorkCollection)

        // Создаем мок для collection
        val mockCollection = mockk<HashMap<String, LabWork>>()
        LabWorkCollection.collection = mockCollection // Без приведения к HashMap

        // Замокаем keys
        every { mockCollection.keys } returns mutableSetOf("1", "2")

        // Замокаем getValue
        val labWork1 = mockk<LabWork>()
        val labWork2 = mockk<LabWork>()
        every { mockCollection.getValue("1") } returns labWork1
        every { mockCollection.getValue("2") } returns labWork2

        // Замокаем getId() для каждого элемента
        every { labWork1.getId() } returns 1
        every { labWork2.getId() } returns 2

        // Выполняем тест
        val key = LabWorkCollection.getKeyById(1)
        assertEquals("1", key)

        unmockkAll()
    }

    @Test
    fun getKeyByPQM() {
        mockkObject(LabWorkCollection)

        // Создаем мок для collection
        val mockCollection = mockk<HashMap<String, LabWork>>()
        LabWorkCollection.collection = mockCollection // Без приведения к HashMap

        // Замокаем keys
        every { mockCollection.keys } returns mutableSetOf("1", "2")

        // Замокаем getValue
        val labWork1 = mockk<LabWork>()
        val labWork2 = mockk<LabWork>()
        every { mockCollection.getValue("1") } returns labWork1
        every { mockCollection.getValue("2") } returns labWork2

        // Замокаем getId() для каждого элемента
        every { labWork1.getPersonalQualitiesMinimum() } returns 40
        every { labWork2.getPersonalQualitiesMinimum() } returns 20

        // Выполняем тест
        val key = LabWorkCollection.getKeyByPQM(40)
        assertEquals("1", key)

        unmockkAll()
    }

    @Test
    fun getMinByNameKey() {
        mockkObject(LabWorkCollection)

        // Создаем мок для collection
        val mockCollection = mockk<HashMap<String, LabWork>>()
        LabWorkCollection.collection = mockCollection // Без приведения к HashMap

        // Замокаем keys
        every { mockCollection.keys } returns mutableSetOf("1", "2")

        // Замокаем getValue
        val labWork1 = mockk<LabWork>()
        val labWork2 = mockk<LabWork>()
        every { mockCollection.getValue("1") } returns labWork1
        every { mockCollection.getValue("2") } returns labWork2

        // Замокаем getId() для каждого элемента
        every { labWork1.getName() } returns "A"
        every { labWork2.getName() } returns "B"

        // Выполняем тест
        val key = LabWorkCollection.getMinByNameKey()
        assertEquals("1", key)

        unmockkAll()
    }

    @Test
    fun getDescendingAuthors() {
        mockkObject(LabWorkCollection)

        // Создаем реальный HashMap
        val realCollection = HashMap<String, LabWork>()

        // Создаем мокированные объекты LabWork и Person
        val labWork1 = mockk<LabWork>()
        val labWork2 = mockk<LabWork>()

        val person1 = Person("A", ZonedDateTime.now(), 2.0, 2.0, Location(1.0, 2f, 3.0))
        val person2 = Person("B", ZonedDateTime.now(), 2.0, 2.0, Location(1.0, 2f, 3.0))

        // Замокаем getAuthor() для каждого LabWork
        every { labWork1.getAuthor() } returns person1
        every { labWork2.getAuthor() } returns person2

        // Добавляем мокированные элементы в HashMap
        realCollection["1"] = labWork2 // "1" соответствует person2 ("A")
        realCollection["2"] = labWork1 // "2" соответствует person1 ("B")

        // Присваиваем реальную коллекцию
        LabWorkCollection.collection = realCollection

        // Выполняем тест
        val result = LabWorkCollection.getDescendingAuthors()

        // Проверяем результат
        assertEquals(listOf(person2, person1), result) // Сортировка по имени автора: "A" перед "B"

        unmockkAll()
        LabWorkCollection.collection.clear()
    }
}
