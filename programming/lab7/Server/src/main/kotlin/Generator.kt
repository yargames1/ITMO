import labWorkClass.*
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Объект для генерации новых значений и объектов.
 */
object Generator {
    /**
     * Генерирует новый уникальный ID для LabWork.
     *
     * @return новый ID, который является максимальным существующим ID + 1.
     */
    fun newId(): Int{
        return LabWorkCollection.getMaxId()+1
    }
    /**
     * Возвращает текущую дату.
     *
     * @return текущая дата в формате LocalDate.
     */
    fun newDate(): LocalDate {
        return LocalDate.now()
    }
    /**
     * Создает новый элемент коллекции, запрашивая поля для ввода через консоль
     *
     * @param id id работы, если нужен. Иначе генерируется автоматически
     *
     * @return Элемент коллекции
     */
    fun newLab(id: Int?, info: List<String>): LabWork {

        val newId = id ?: newId()
        val creationDate = newDate()

        val name = info[0]
        val coord1 = info[1]
        val coord2 = info[2]
        val minimalPoint = info[3].toLong()
        val personalQualitiesMinimum = info[4].toLong()
        val difficulty = Difficulty.valueOf(info[5])
        val authorName = info[6]
        val birthday = ZonedDateTime.parse(info[7])
        val height = info[8].toDouble()
        val weight = info[9].toDouble()
        val locx = info[10].toDouble()
        val locy = info[11].toFloat()
        val locz = info[12].toDouble()

        val coordinates = Coordinates(coord1.toInt(), coord2.toInt())

        val location = Location(locx, locy, locz)

        val author = Person(authorName, birthday, height, weight, location)

        return LabWork(
            newId,
            name,
            coordinates,
            creationDate,
            minimalPoint,
            personalQualitiesMinimum,
            difficulty,
            author
        )

    }
}