package labWorkClass

import java.time.ZonedDateTime
/**
 * Объект управляет коллекцией лабораторных работ.
 * Хранит данные в виде HashMap, где ключ — строка, а значение — объект LabWork.
 */
object LabWorkCollection {
    var collection = HashMap<String, LabWork>()

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
        var name: String
        while (true) {
            name = readln()
            if (name.trim() != ""){
                break
            }
            else {
                println("Произошла ошибка при вводе данных, строка не должна быть пустой")
            }
        }

        println("Введите координаты работы в формате x y")
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
        }
        val coordinates = Coordinates(coord[0],coord[1])

        val creationDate = Generator.newDate()

        println("Введите минимальную оценку за работу")
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
        }

        println("Введите минимальную оценку за работу, приемлемую для ученика")
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
        }

        println("Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS")
        var difficulty: Difficulty
        while (true) {
            try {
                difficulty = Difficulty.valueOf(readln())
                break
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, попробуйте снова")
            }
        }

        println("Введите имя автора работы")
        var authorName: String
        while (true) {
                authorName = readln()
                if (authorName.trim() != ""){
                    break
                }
                else {
                    println("Произошла ошибка при вводе данных, строка не должна быть пустой")
                }
        }

        println("Введите дату рождения автора в формате ISO-8601 calendar system. Пример: 2007-12-03T10:15+01:00[Europe/Paris]")
        var birthday: ZonedDateTime
        while (true) {
            try {
                birthday = ZonedDateTime.parse(readln())
                break
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, попробуйте снова")
            }
        }

        println("Введите высоту автора")
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
        }

        println("Введите вес автора")
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
        }

        println("Введите координаты автора в формате x y z")
        var coord_X: Double
        var coord_Y: Float
        var coord_Z: Double
        while (true) {
            try {
                val coord_author = readln().split(" ")
                if (coord_author.size == 3){
                    coord_X = coord_author[0].toDouble()
                    coord_Y = coord_author[0].toFloat()
                    coord_Z = coord_author[0].toDouble()
                    break
                }
                else{println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y z")}
            }
            catch (e: Exception){
                println("Произошла ошибка при вводе данных, x - Double, y - Float, z - Double")
            }
        }
        val location = Location(coord_X, coord_Y, coord_Z)
        val author = Person(authorName, birthday, height, weight, location)
        return LabWork(newId, name, coordinates, creationDate, minimalPoint, personalQualitiesMinimum, difficulty, author)
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
        val difficulty = Difficulty.valueOf(tokens[5])


        // Введите имя автора работы
        val authorName = tokens[6]


        // Введите дату рождения автора в формате ISO-8601 calendar system. Пример: 2007-12-03T10:15+01:00[Europe/Paris]
        val birthday =  ZonedDateTime.parse(tokens[7].toString())


        // Введите высоту автора
        val height = tokens[8].toDouble()

        // Введите вес автора
        val weight = tokens[9].toDouble()

        // Введите координаты автора в формате x y z
        val coordX = tokens[10].toDouble()
        val coordY = tokens[11].toFloat()
        val coordZ = tokens[12].toDouble()

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