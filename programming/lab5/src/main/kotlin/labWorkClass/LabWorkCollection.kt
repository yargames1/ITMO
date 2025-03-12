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
        val list = mutableListOf<Int>()
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

        val newId: Int = id ?: Generator.newId()

        println("Введите имя работы")
        print("> ")
        val name = Validator.str(readln())

        println("Введите координаты работы в формате x y")
        print("> ")
        val coord = Validator.coords(readln())
        val coordinates = Coordinates(coord[0],coord[1])

        val creationDate = Generator.newDate()

        println("Введите минимальную оценку за работу")
        print("> ")
        val minimalPoint = Validator.longMoreThanZero(readln())

        println("Введите минимальную оценку за работу, приемлемую для ученика")
        print("> ")
        val personalQualitiesMinimum = Validator.longMoreThanZero(readln())
        println("Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS")
        print("> ")
        val difficulty = Validator.enum(readln().uppercase())


        println("Введите имя автора работы")
        print("> ")
        val authorName = Validator.str(readln())

        println("Введите дату рождения автора. Пример 2006.04.13")
        print("> ")
        val bithDate = Validator.date(readln())

        println("Введите время рождения автора. Пример 10:24")
        print("> ")
        val birthTime = Validator.time(readln())

        println("Введите часовой пояс места рождения автора. Пример +01:00")
        print("> ")

        val Zone = Validator.zone(readln())

        val birthday = ZonedDateTime.parse("${bithDate.replace(".","-")}T${birthTime}$Zone")
        println("Введите высоту автора")
        print("> ")
        val height = Validator.doubleMoreThanZero(readln())

        println("Введите вес автора")
        print("> ")
        val weight = Validator.doubleMoreThanZero(readln())

        println("Введите координаты автора в формате x y z")
        print("> ")
        val location = Validator.loc(readln())
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

        val newId: Int = id ?: Generator.newId()

        // Введите имя работы
        val name = Validator.str(tokens[0])


        // Введите координаты работы в формате x y
        val coord = Validator.coords(tokens[1]+" "+tokens[2], auto = true)

        val coordinates = Coordinates(coord[0],coord[1])

        val creationDate = Generator.newDate()

        // Введите минимальную оценку за работу
        val minimalPoint = Validator.longMoreThanZero(tokens[3])


        // Введите минимальную оценку за работу, приемлемую для ученика
        val personalQualitiesMinimum =Validator.longMoreThanZero(tokens[4])

        // Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS
        val difficulty = Validator.enum(tokens[5])


        // Введите имя автора работы
        val authorName = Validator.str(tokens[6])


        // Введите дату рождения автора. Пример 2006.04.13"
        val bithDate = Validator.date(tokens[7])

        // Введите время рождения автора. Пример 10:24"
        val birthTime = Validator.time(tokens[8])

        // Введите часовой пояс места рождения автора. Пример +01:00"
        val Zone = Validator.zone(tokens[9])

        val birthday = ZonedDateTime.parse("${bithDate.replace(".","-")}T${birthTime}$Zone")

        // Введите высоту автора
        val height = Validator.doubleMoreThanZero(tokens[10])

        // Введите вес автора
        val weight = Validator.doubleMoreThanZero(tokens[11])

        val location = Validator.loc(tokens[12]+" "+tokens[13]+" "+tokens[14])
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
        val names = mutableListOf<String>()
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