package Server.managersPackage

import Server.Log.logger
import Server.labWorkClass.*
import java.time.LocalDate
import java.time.ZonedDateTime
import kotlin.io.path.*
/**
 * Объект для управления загрузкой и сохранением данных лабораторных работ.
 *
 * Хранит методы для работы с файлом CSV, включая чтение и запись коллекции объектов LabWork.
 */
object DataManager {
    /**
     * Загружает данные из указанного файла в коллекцию LabWork.
     *
     * @param filename имя файла, содержащего данные в формате CSV.
     *
     * @return HashMap с ключами-строками и объектами LabWork.
     */
    fun load(): HashMap<String, LabWork> {
        val filename = "data.csv"
        val filedir = "files"
        val newList = HashMap<String, LabWork>()
        try {
            val path = Path(filedir, filename) // Преобразуем строку в путь
            if (path.exists()) {
                path.readLines().drop(1).forEach { line ->
                    try {
                        val fields = line.split(",").map { it.trim() }
                        val key = fields.first()
                        val labWork = parsefile(fields.drop(1))
                        newList.put(key, labWork)
                    } catch (e: Exception) {
                        logger.info("Ошибка при разборе строки: $line\n${e.message}")
                    }
                }
                logger.info("Данные загружены")
            } else {
                logger.info("Файл не найден: $filedir/$filename")
                logger.info("Программа будет запущена без исходных данных")
            }
        } catch (e: Exception) {
            logger.info("Ошибка при чтении файла: $filedir/$filename ${e.message}")
        }
        return newList
    }
    /**
     * Сохраняет коллекцию LabWork в файл data.csv.
     *
     * @param labWorks коллекция лабораторных работ для сохранения.
     */
    fun save(labWorks: HashMap<String, LabWork>) {
        val filename = "data.csv"
        val filedir = "files"
        try {
            val path = Path(filedir, filename)
            if (!path.exists()) {
                if (!Path(filedir).exists()) {
                    Path(filedir).createDirectory()
                }
                path.createFile()
            }
            var text = "key,id,name,cordX,cordY,creationDate,minimalPoint,personalQualitiesMinimum,difficulty," +
                    "name,birthday,height,weight,locX,locY,locZ"
            for(key: String in labWorks.keys){
                text += "\n${key},${labWorks.get(key)}"
            }
            path.outputStream().write(text.toByteArray())
            logger.info("Данные успешно записаны в файл data.csv")
        } catch (e: Exception) {
            logger.info("Ошибка при записи в файл: ${e.message}")
        }
    }
    /**
     * Преобразует строку данных в объект LabWork.
     *
     * @param fields список значений из строки CSV.
     *
     * @return объект LabWork.
     *
     * @throws Exception если количество полей недостаточно или данные некорректны.
     */
    @Throws(Exception::class)
    private fun parsefile(fields: List<String>): LabWork {
        if (fields.size < 14) throw Exception("Ошибка при чтении файла: недостаточно данных")

        val id: Int = fields[0].toInt()
        val name = fields[1] //Поле не может быть null, Строка не может быть пустой
        val coordX: Int = fields[2].toInt()
        val coordY: Int = fields[3].toInt()
        val creationDate = LocalDate.parse(fields[4])
        val minimalPoint: Long = fields[5].toLong()
        val personalQualitiesMinimum: Long = fields[6].toLong()
        val difficulty = Difficulty.valueOf(fields[7])
        val authorName = fields[8]
        val birthday = ZonedDateTime.parse(fields[9]) //Поле не может быть null
        val height: Double = fields[10].toDouble() //Поле не может быть null, Значение поля должно быть больше 0
        val weight: Double = fields[11].toDouble() //Значение поля должно быть больше 0
        val locX: Double = fields[12].toDouble() //Поле не может быть null
        val locY: Float = fields[13].toFloat() //Поле не может быть null
        val locZ: Double = fields[14].toDouble()

        val location = Location(locX, locY, locZ)
        val author = Person(authorName, birthday, height, weight, location)
        val coordinates = Coordinates(coordX, coordY)

        return LabWork(
            id,
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