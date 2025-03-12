package labWorkClass

import java.time.ZonedDateTime
/**
 * Объект управляет коллекцией лабораторных работ.
 * Хранит данные в виде HashMap, где ключ — строка, а значение — объект LabWork.
 */
object LabWorkCollection {
    var collection = HashMap<String, LabWork>()
    val inicializationDate = Generator.newDate()

    /**
     * Возвращает максимальный id, которые используются
     *
     * @return id, число
     */
    fun getMaxId(): Int{
        var list = mutableListOf<Int>()
        for (values in collection.values){
            list.add(values.getId())
        }
        return list.maxOrNull() ?: -1
    }

    /**
     * Создает новый элемент коллекции, запрашивая поля для ввода через консоль
     *
     * @param id id работы, если нужен. Иначе генерируется автоматически
     *
     * @return Элемент коллекции
     */
    fun newLab(id: Int?): LabWork{

        var newId: Int = id ?: Generator.newId()

        println("Введите имя работы")
        print("> ")
        var name: String
        while (true) {
            name = readln()
            if (name.trim() != ""){
                break
            }
            else {
                println("Произошла ошибка при вводе данных, строка не должна быть пустой")
            }
            print("> ")
        }

        println("Введите координаты работы в формате x y")
        print("> ")
        var coord: List<Int>
        while (true) {
            try {
                coord = readln().split(" ").map { it.toInt() }
                if ((coord.size == 2)and (coord[1]<=858)){
                    break
                }
                else{println("Произошла ошибка при вводе данных, у должен быть не больше 858")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
            }
            print("> ")
        }
        val coordinates = Coordinates(coord[0],coord[1])

        val creationDate = Generator.newDate()

        println("Введите минимальную оценку за работу")
        print("> ")
        var minimalPoint: Long
        while (true) {
            try {
                minimalPoint = readln().toLong()
                if (minimalPoint > 0){
                    break
                }
                else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, это должно быть число")
            }
            print("> ")
        }

        println("Введите минимальную оценку за работу, приемлемую для ученика")
        print("> ")
        var personalQualitiesMinimum: Long
        while (true) {
            try {
                personalQualitiesMinimum = readln().toLong()
                if (personalQualitiesMinimum > 0){
                    break
                }
                else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, это должно быть число")
            }
            print("> ")
        }

        println("Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS")
        print("> ")
        var difficulty: Difficulty
        while (true) {
            try {
                difficulty = Difficulty.valueOf(readln().uppercase())
                break
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, попробуйте снова")
            }
            print("> ")
        }

        println("Введите имя автора работы")
        print("> ")
        var authorName: String
        while (true) {
                authorName = readln()
                if (authorName.trim() != ""){
                    break
                }
                else {
                    println("Произошла ошибка при вводе данных, строка не должна быть пустой")
                }
            print("> ")
        }

        println("Введите дату рождения автора. Пример 2006.04.13")
        print("> ")
        var bithDate: String

        while (true) {
            bithDate = readln()
            if (datePatternCheck(bithDate)) {
                break
            }
            print("> ")
        }

        println("Введите время рождения автора. Пример 10:24")
        print("> ")
        var birthTime: String

        while (true) {
            birthTime = readln()
            if (timePatternCheck(birthTime)){
                break
            }
            print("> ")
        }

        println("Введите часовой пояс места рождения автора. Пример +01:00")
        print("> ")

        var Zone: String

        while (true) {
            Zone = readln()
            if (ZonePatternCheck(Zone)){
                break
            }
            print("> ")
        }
        val birthday = ZonedDateTime.parse("${bithDate.replace(".","-")}T${birthTime}$Zone")
        println("Введите высоту автора")
        print("> ")
        var height: Double
        while (true) {
            try {
                height = readln().toDouble()
                if (height > 0){
                    break
                }
                else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, это должно быть число")
            }
            print("> ")
        }

        println("Введите вес автора")
        print("> ")
        var weight: Double
        while (true) {
            try {
                weight = readln().toDouble()
                if (weight > 0){
                    break
                }
                else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, это должно быть число")
            }
            print("> ")
        }

        println("Введите координаты автора в формате x y z")
        print("> ")
        var coord_X: Double
        var coord_Y: Float
        var coord_Z: Double
        while (true) {
            try {
                val coord_author = readln().split(" ")
                if (coord_author.size == 3){
                    coord_X = coord_author[0].toDouble()
                    coord_Y = coord_author[1].toFloat()
                    coord_Z = coord_author[2].toDouble()
                    break
                }
                else{println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y z")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, x - Double, y - Float, z - Double")
            }
            print("> ")
        }
        val location = Location(coord_X, coord_Y, coord_Z)
        val author = Person(authorName, birthday, height, weight, location)
        return LabWork(newId, name, coordinates, creationDate, minimalPoint, personalQualitiesMinimum, difficulty, author)
    }

    private fun timePatternCheck(time: String): Boolean {
        val timePattern = """^\d{2}:\d{2}$""".toRegex()

        if (time.matches(timePattern)) {
            val (hours, minutes) = time.split(":").map { it.toInt() }
            if (hours in 0..24) {
                if (minutes in 0..59) {
                    return true
                } else {
                    println("Неверное количество минут. Попробуйте снова.")
                }
            } else {
                println("Неверное количество часов. Попробуйте снова.")
            }
        } else {
            println("Неверный формат времени. Попробуйте снова.")
        }
        return false
    }
    private fun ZonePatternCheck(time: String): Boolean {
        val timePattern = """^[+-]\d{2}:\d{2}$""".toRegex()

        if (time.matches(timePattern)) {
            val (hours, minutes) = time.split(":").map { it.toInt() }
            if (hours in -18..18) {
                if (minutes in 0..59) {
                    return true
                } else {
                    println("Неверное количество минут. Попробуйте снова.")
                }
            } else {
                println("Неверное количество часов. Попробуйте снова.")
            }
        } else {
            println("Неверный формат времени. Попробуйте снова.")
        }
        return false
    }

    private fun datePatternCheck(date:String):Boolean{
        val datePattern = """^\d{4}\.\d{2}\.\d{2}$""".toRegex()
        val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (date.matches(datePattern)) {
            val (year, month, day) = date.split(".").map { it.toInt() }
            if (month in 1..12) {
                if (day in 1..daysInMonth[month - 1]) {
                    return true
                } else {
                    println("Неверное количество дней в месяце. Попробуйте снова.")
                }
            } else {
                println("Месяц должен быть в пределах от 01 до 12. Попробуйте снова.")
            }
        } else {
            println("Неверный формат даты. Попробуйте снова.")
        }
        return false
    }
    /**
     * Создает новый элемент коллекции, получая поля через аргумент (для автоматического выполнения)
     *
     * @param id id работы, если нужен. Иначе генерируется автоматически
     * @param tokens Список из аргументов, из которых формируются поля
     *
     * @return Элемент коллекции
     */
    fun autoNewLab(id: Int?, tokens: List<String>): LabWork{

        var newId: Int = id ?: Generator.newId()

        // Введите имя работы
        val name = tokens[0]


        // Введите координаты работы в формате x y
        val coord = listOfNotNull(tokens[1].toInt(), tokens[2].toInt())

        val coordinates = Coordinates(coord[0],coord[1])

        val creationDate = Generator.newDate()

        // Введите минимальную оценку за работу
        val minimalPoint = tokens[3].toLong()


        // Введите минимальную оценку за работу, приемлемую для ученика
        val personalQualitiesMinimum = tokens[4].toLong()

        // Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS
        val difficulty = Difficulty.valueOf(tokens[5].uppercase())


        // Введите имя автора работы
        val authorName = tokens[6]


        // Введите дату рождения автора. Пример 2006.04.13"
        val bithDate = tokens[7]

        // Введите время рождения автора. Пример 10:24"
        val birthTime = tokens[8]

        // Введите часовой пояс места рождения автора. Пример +01:00"
        val Zone = tokens[9]

        val birthday = ZonedDateTime.parse("${bithDate.replace(".","-")}T${birthTime}$Zone")

        // Введите высоту автора
        val height = tokens[10].toDouble()

        // Введите вес автора
        val weight = tokens[11].toDouble()

        // Введите координаты автора в формате x y z
        val coordX = tokens[12].toDouble()
        val coordY = tokens[13].toFloat()
        val coordZ = tokens[14].toDouble()

        val location = Location(coordX, coordY, coordZ)
        val author = Person(authorName, birthday, height, weight, location)
        return LabWork(newId, name, coordinates, creationDate, minimalPoint, personalQualitiesMinimum, difficulty, author)
    }

    /**
     * Выдает значение ключа по id
     *
     * @param id id элемента
     *
     * @return Ключ элемента
     */
    fun getKeyById(id: Int): String{
        for (key in collection.keys){
            if (collection.getValue(key).getId() == id){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает значение ключа по personalQualitiesMinimum
     *
     * @param personalQualitiesMinimum personalQualitiesMinimum элемента
     *
     * @return Ключ элемента
     */
    fun getKeyByPQM(personalQualitiesMinimum: Long): String{
        for (key in collection.keys){
            if (collection.getValue(key).getPersonalQualitiesMinimum() == personalQualitiesMinimum){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает значение ключа по имени, берет минимальное
     *
     * @return Ключ элемента
     */
    fun getMinByNameKey(): String{
        var names = mutableListOf<String>()
        for (key in collection.keys){
            names.add(collection.getValue(key).getName())
        }
        val minName = names.minOrNull()
        for(key in collection.keys){
            if (minName == collection.getValue(key).getName()){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает список авторов, отсортированный по убыванию
     *
     * @return Список авторов
     */
    fun getDescendingAuthors(): List<Person>{
        return collection.entries.sortedByDescending { it.value.getAuthor() }.map { it.value.getAuthor() }
    }
}