import labWorkClass.*
import managersPackage.IOManager
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Объект для генерации уникальных идентификаторов и дат.
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
    fun newLab(id: Int?): LabWork {
        val auto = FlagController.getAutoFlag()
        val newId = id ?: newId()

        fun readValidatedInput(prompt: String, validator: (String) -> Boolean, errorMessage: String, retryMessage: String): String {
            IOManager.send(prompt)
            var input = IOManager.read()
            while (!validator(input)) {
                if (auto) throw Exception(errorMessage)
                IOManager.send(retryMessage)
                input = IOManager.read()
            }
            return input
        }

        val name = readValidatedInput(
            "Введите имя работы", Validator::str,
            "Неверно указано имя работы, создание объекта прервано",
            "Некорректное имя, попробуйте снова."
        )

        val coord = readValidatedInput(
            "Введите координаты работы в формате x y", Validator::coords,
            "Неверно указаны координаты работы, создание объекта прервано",
            "Ошибка в формате координат. Введите снова (пример: 10 20)."
        )
        val coords = coord.split(" ").map { it.toInt() }
        val coordinates = Coordinates(coords[0], coords[1])

        val creationDate = newDate()

        val minimalPoint = readValidatedInput(
            "Введите минимальную оценку за работу", Validator::longMoreThanZero,
            "Неверно указана минимальная оценка за работу, создание объекта прервано",
            "Введите число больше 0."
        ).toLong()

        val personalQualitiesMinimum = readValidatedInput(
            "Введите минимальную оценку за работу, приемлемую для ученика", Validator::longMoreThanZero,
            "Неверно указана минимальная оценка за работу, приемлемая для ученика, создание объекта прервано",
            "Число должно быть больше 0. Попробуйте снова."
        ).toLong()

        val difficulty = Difficulty.valueOf(
            readValidatedInput(
                "Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS", Validator::enum,
                "Неверно указан уровень сложности работы, создание объекта прервано",
                "Ошибка. Введите один из предложенных уровней сложности: EASY, NORMAL, VERY_HARD, HOPELESS."
            ).uppercase()
        )

        val authorName = readValidatedInput(
            "Введите имя автора работы", Validator::str,
            "Неверно указано имя автора работы, создание объекта прервано",
            "Имя не должно быть пустым. Попробуйте снова."
        )

        val birthDate = readValidatedInput(
            "Введите дату рождения автора (формат: 2006.04.13)", Validator::date,
            "Неверно указана дата рождения автора, создание объекта прервано",
            "Ошибка в формате даты. Введите снова (пример: 2006.04.13)."
        )

        val birthTime = readValidatedInput(
            "Введите время рождения автора (формат: 10:24)", Validator::time,
            "Неверно указано время рождения автора, создание объекта прервано",
            "Ошибка в формате времени. Введите снова (пример: 10:24)."
        )

        val zone = readValidatedInput(
            "Введите часовой пояс места рождения автора (формат: +01:00)", Validator::zone,
            "Неверно указан часовой пояс места рождения автора, создание объекта прервано",
            "Ошибка в формате часового пояса. Введите снова (пример: +01:00)."
        )

        val birthday = ZonedDateTime.parse("${birthDate.replace(".", "-")}T${birthTime}$zone")

        val height = readValidatedInput(
            "Введите высоту автора", Validator::doubleMoreThanZero,
            "Неверно указана высота автора, создание объекта прервано",
            "Введите число больше 0."
        ).toDouble()

        val weight = readValidatedInput(
            "Введите вес автора", Validator::doubleMoreThanZero,
            "Неверно указан вес автора, создание объекта прервано",
            "Введите число больше 0."
        ).toDouble()

        val locationInput = readValidatedInput(
            "Введите координаты автора в формате x y z", Validator::loc,
            "Неверно указаны координаты автора, создание объекта прервано",
            "Ошибка в формате координат. Введите снова (пример: 10.5 20.3 30.7)."
        )
        val loc = locationInput.split(" ")
        val location = Location(loc[0].toDouble(), loc[1].toFloat(), loc[2].toDouble())

        val author = Person(authorName, birthday, height, weight, location)

        return LabWork(newId, name, coordinates, creationDate, minimalPoint, personalQualitiesMinimum, difficulty, author)
    }
}